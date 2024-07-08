package com.wyl.backend.keda.classes.auxiliary.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.wyl.backend.keda.classes.auxiliary.SQL.studentMapper;
import com.wyl.backend.keda.classes.auxiliary.websocket.MyWebSocketHandler;
import net.sf.jsqlparser.expression.StringValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RequestMapping(value = "/info")
@RestController
public class HttpPostRequest {
    //传入学生的uid和题目id，返回做题情况数据
    @Autowired
    private studentMapper studentMapper;

    @Autowired
    private MyWebSocketHandler webSocketHandler;

    private int[] alluids;
    @Scheduled(fixedRate = 20000) // 每20秒执行一次
    public void sendPeriodicMessages() {
        try {
            alluids = getStudentByClass(); // 假设这是已有的数组
            alluids = addStundent(alluids,new int[]{4462});
            // 将数组转换为ArrayList


            ArrayList<student> temp = extractAllScore(getSolvedSituation(alluids,geAllproblems().toArray(new Integer[0])));

//            for(student x : temp){
//                System.out.println(x.uid + "" + x.pid + "测试" + x.score);
//            }
            insertToDataBase(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String getSolvedSituation(int uids[],Integer pids[]) {//查询做题情况
        String url = "https://tea.kedaoi.cn/prod-api/oj/problem/situation/ojUserAndProblem/system";
        HttpURLConnection connection = null;

        try {
            // Create URL and open connection
            URL urlObject = new URL(url);
            connection = (HttpURLConnection) urlObject.openConnection();

            // Set request method and properties
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjU0MTFkYzc0LTdhMGQtNDI2Mi1hNDgwLTY4NWI4NjVmMmUwMCJ9.jWzwAXviApOu2HwIWmhi84rYYyxAtDaQZvTFu80BTaVGE-AWL0AfnALYS4qKKUpra6ZsEVHnPXiEEOtb2dIMaA");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);

            // Create JSON payload
            JSONObject payload = new JSONObject();
            payload.put("uids",uids);
            payload.put("pids", pids);
//            for(int x : uids){
//                System.out.println(x);
//            }
            // Send POST request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(payload.toString());
            wr.flush();
            wr.close();

            // Check response code
//            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code : " + responseCode);

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
//            System.out.println(response);
            // Print result
//            System.out.println(response);
            return String.valueOf(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public  void sendMessageToWeb(student x) throws IOException {
        webSocketHandler.broadcast(x);
        System.out.println("定时任务执行，消息已发送");
    }
    public  void insertToDataBase(ArrayList<student> info) throws IOException {
        ArrayList<student> had = (ArrayList<student>) studentMapper.selectList(null);
        // 找出info中有而had中没有的元素
        Set<student> infoOnly = new HashSet<>(info);
        infoOnly.removeAll(had);

        System.out.println("Students in info but not in had: " + infoOnly);
        sendMessageToWeb(info.get(0));
        for(student x : infoOnly){
            if(x.getScore() == 100){
                studentMapper.insert(x);
                sendMessageToWeb(x);
            }

        }
    }
    public int[] addStundent(int a[],int b[]){
        ArrayList<Integer> uidList = new ArrayList<>();
        for (int uid : a) {
            uidList.add(uid);
        }
        for(int uid : b){
            uidList.add(uid);
        }

        // 将ArrayList转换回数组
        a = uidList.stream().mapToInt(i -> i).toArray();
        return a;
    }
    public  ArrayList<student> extractAllScore(String jsonResponse) {//清洗同学们的题目得分
        ArrayList<student> ans = new ArrayList<>();
        // 解析JSON响应
        JSONObject jsonObject = new JSONObject(jsonResponse);
//        JSONArray rows = jsonObject.getJSONArray("rows");
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject table = data.getJSONObject("table");

        int uids[] = alluids;
//        uids[0] = 4462;
        List<Integer> pids = geAllproblems();
        // 遍历每个row元素
        for (int i = 0; i < uids.length; i++) {
            ArrayList<student> temp = new ArrayList<>();
            Set<Integer> set = new HashSet<Integer>(){};
            for (int j = 0 ;j < pids.size() ;j ++){
               try{
                   JSONObject tp = table.getJSONObject(String.valueOf(uids[i]))
                           .getJSONObject(String.valueOf(pids.get(j)));
                   if(set.contains(pids.get(j))){
                       continue;
                   }
                   set.add(pids.get(j));
                   ans.add(new student(uids[i],(Integer) tp.get("score"), pids.get(j),String.valueOf(tp.get("judgeAt"))));
               }catch (JSONException e){

               }

            }

        }
        return ans;
    }


    public  List<Integer> extractAllPids(String jsonResponse) {//所有题单里获取题目的id
        List<Integer> allPids = new ArrayList<>();

        // 解析JSON响应
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray rows = jsonObject.getJSONArray("rows");

        // 遍历每个row元素
        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            JSONArray dags = row.getJSONArray("dag");

            // 遍历每个dag元素
            for (int j = 0; j < dags.length(); j++) {
                JSONObject dag = dags.getJSONObject(j);
                JSONArray pids = dag.getJSONArray("pids");

                // 遍历并添加每个pid到列表中
                for (int k = 0; k < pids.length(); k++) {
                    allPids.add(pids.getInt(k));
                }
            }
        }

        return allPids;
    }

    public  int[] extractIds(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray rows = jsonObject.getJSONArray("rows");
        int[] ids = new int[rows.length()];

        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            ids[i] = row.getInt("_id"); // 假设_id总是存在并且是整数

        }

        return ids;
    }

    public  int[] getStudentByClass() {
        String url = "https://tea.kedaoi.cn/prod-api/oj/user/listByGroup?pageNum=1&pageSize=200";
        HttpURLConnection connection = null;

        try {
            // Create URL and open connection
            URL urlObject = new URL(url);
            connection = (HttpURLConnection) urlObject.openConnection();

            // Set request method and properties
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjU0MTFkYzc0LTdhMGQtNDI2Mi1hNDgwLTY4NWI4NjVmMmUwMCJ9.jWzwAXviApOu2HwIWmhi84rYYyxAtDaQZvTFu80BTaVGE-AWL0AfnALYS4qKKUpra6ZsEVHnPXiEEOtb2dIMaA");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            connection.setDoOutput(true);
            JSONArray payload = new JSONArray();
            String temp[] = new String[]{
                    "wrtoB9DgAAB9cdhg6lRV6VzYpSbG2iRQ"
            };
            for(int i = 0 ;i < temp.length ;i ++){
                payload.put(temp[i]);
            }


            // Create JSON payload

//            payload.put("uids", new int[]{4462});
//            payload.put("pids", new int[]{1, 5, 13, 148, 519, 521, 1421, 1422});
//            payload.put("class",new String[]{"wrtoB9DgAAJA8bHczfTNtIq3ey82Rscg"});
            // Send POST request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(payload.toString());
            wr.flush();
            wr.close();


            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print result

            return extractIds(String.valueOf(response));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new int[0];
    }

    public  List<Integer> geAllproblems() {
        String url = "https://tea.kedaoi.cn/prod-api/oj/training/all/system";
        HttpURLConnection connection = null;

        try {
            // Create URL with query parameters
            URL urlObject = new URL(url);
            connection = (HttpURLConnection) urlObject.openConnection();

            // Set request method and properties
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjU0MTFkYzc0LTdhMGQtNDI2Mi1hNDgwLTY4NWI4NjVmMmUwMCJ9.jWzwAXviApOu2HwIWmhi84rYYyxAtDaQZvTFu80BTaVGE-AWL0AfnALYS4qKKUpra6ZsEVHnPXiEEOtb2dIMaA");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            // Check response code and read response
            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();



            return extractAllPids(String.valueOf(response));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @GetMapping(value = "/getclass")//获取所有班级信息
    public  String updateClass() {
        String url = "https://tea.kedaoi.cn/prod-api/group/list?pageNum=1&pageSize=800";
        HttpURLConnection connection = null;

        try {
            // Create URL with query parameters
            URL urlObject = new URL(url);
            connection = (HttpURLConnection) urlObject.openConnection();

            // Set request method and properties
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br, zstd");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjU0MTFkYzc0LTdhMGQtNDI2Mi1hNDgwLTY4NWI4NjVmMmUwMCJ9.jWzwAXviApOu2HwIWmhi84rYYyxAtDaQZvTFu80BTaVGE-AWL0AfnALYS4qKKUpra6ZsEVHnPXiEEOtb2dIMaA");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            // Check response code and read response
            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


//    public  void main(String[] args) throws IOException {
//        ArrayList<student> temp = extractAllScore(getSolvedSituation(getStudentByClass(),geAllproblems().toArray(new Integer[0])));
////        myWebSocketHandler.broadcast("后端给前端发消息");
////        getStudentByClass();
//    }

}
