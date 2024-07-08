// src/api.js
import { SERVER_URL } from '@/js/functions/config';
import axios from 'axios';
axios.defaults.baseURL = '/prod-api';

const cookie = "sidebarStatus=0; _ga=GA1.1.956203062.1709896264; _hjSessionUser_2313593=eyJpZCI6ImJiYjZlZDJiLTlmY2YtNWYzMC05Yjk0LThkNDkwMGRmNzU5MCIsImNyZWF0ZWQiOjE3MDk4OTYyNjYyODYsImV4aXN0aW5nIjp0cnVlfQ==; _ga_3FTEQXFKNF=GS1.1.1719045902.4.1.1719046025.60.0.0; Hm_lvt_ca5af897b6e9c781b4f9d2037e9c8d83=1719381287; HMACCOUNT=BFD4D939A1F9357E; Admin-Token=eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjFjYzdlZGU5LWE0MmMtNDVjMy05ZDdkLTBhYzg0ZmNmMjRiMyJ9.bwbM4UBKCh6Ulf_E8Mqnbms87S8TLz5TtABLvu8lZbaQyvVU5tk3d413OaoUdnW8kJqE8Y1chtN2vhOXXUKiBQ; Hm_lpvt_ca5af897b6e9c781b4f9d2037e9c8d83=1720347265";

const authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjFjYzdlZGU5LWE0MmMtNDVjMy05ZDdkLTBhYzg0ZmNmMjRiMyJ9.bwbM4UBKCh6Ulf_E8Mqnbms87S8TLz5TtABLvu8lZbaQyvVU5tk3d413OaoUdnW8kJqE8Y1chtN2vhOXXUKiBQ"

/**
 * 发送请求的函数
 * @param {string} authorization - Authorization 的 Bearer 令牌
 * @param {string} cookie - Cookie 信息
 * @param {Object} data - 请求体数据
 * @returns {Promise<Object>} - 返回的 JSON 数据
 */
export async function fetchProblemSituation(data) {
  // 目标网址
  
 
  try {
    // 发送请求
    const response = await axios.post(url, data, { headers: headers });

    if (response.status === 200) {
      // 返回 JSON 数据
      return response.data;
    } else {
      throw new Error(`请求失败，状态码: ${response.status}`);
    }
  } catch (error) {
    console.error(`请求出错: ${error}`);
    throw error;
  }
}

export async function getClass(name) {
  // 目标网址
  

  try {
    // 发送请求
    const response = await axios.get(`${SERVER_URL}/info/getclass`);

    if (response.status === 200) {
      // 返回 JSON 数据
      
      return response.data.rows.map(row => ({
        label: row.className || row.name, // 使用 className，如果不存在则使用 name
        value: row.id
      }));
    } else {
      throw new Error(`请求失败，状态码: ${response.status}`);
    }
  } catch (error) {
    console.error(`请求出错: ${error}`);
    throw error;
  }
}

export async function getStudents(classId) {
  // 目标网址
  const url = "/oj/user/listByGroup?pageNum=1&pageSize=100";

  // 请求标头
  const headers = {
    "Accept": "application/json, text/plain, */*",
    "Accept-Encoding": "gzip, deflate, br, zstd",
    "Accept-Language": "zh-CN,zh;q=0.9",
    "Authorization": { authorization },
    "Cache-Control": "no-cache",
    "Connection": "keep-alive",
    "Content-Type": "application/json;charset=UTF-8",
    "Cookie": cookie,
    "Host": "tea.kedaoi.cn",
    "Origin": "",
    "Pragma": "no-cache",
    "Referer": "/learning_feedback/problemSituationList",
    "Sec-Fetch-Dest": "empty",
    "Sec-Fetch-Mode": "cors",
    "Sec-Fetch-Site": "same-origin",
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
    "sec-ch-ua": '"Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"',
    "sec-ch-ua-mobile": "?0",
    "sec-ch-ua-platform": '"Windows"'
  };

  try {
    // 发送请求
    const response = await axios.post(url,classId, { headers: headers });

    if (response.status === 200) {
      // 返回 JSON 数据
      return response.data.rows.map(row => ({
        label: row._id + " " +  row.uname, // 使用 className，如果不存在则使用 name
        value: row._id
      }));
    } else {
      throw new Error(`请求失败，状态码: ${response.status}`);
    }
  } catch (error) {
    console.error(`请求出错: ${error}`);
    throw error;
  }
}

export async function getProblemList() {
  // 目标网址
  const url = "/oj/training/all/system";

  // 请求标头
  const headers = {
    "Accept": "application/json, text/plain, */*",
    "Accept-Encoding": "gzip, deflate, br, zstd",
    "Accept-Language": "zh-CN,zh;q=0.9",
    "Authorization": { authorization },
    "Cache-Control": "no-cache",
    "Connection": "keep-alive",
    "Content-Type": "application/json;charset=UTF-8",
    "Cookie": cookie,
    "Host": "tea.kedaoi.cn",
    "Origin": "",
    "Pragma": "no-cache",
    "Referer": "/learning_feedback/problemSituationList",
    "Sec-Fetch-Dest": "empty",
    "Sec-Fetch-Mode": "cors",
    "Sec-Fetch-Site": "same-origin",
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
    "sec-ch-ua": '"Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"',
    "sec-ch-ua-mobile": "?0",
    "sec-ch-ua-platform": '"Windows"'
  };

  try {
    // 发送请求
    const response = await axios.get(url,{ headers: headers });

    if (response.status === 200) {
      console.log(response.data)
      return "";
      // 返回 JSON 数据
      return cleanProblemListData(response.data);
     
    } else {
      throw new Error(`请求失败，状态码: ${response.status}`);
    }
  } catch (error) {
    console.error(`请求出错: ${error}`);
    throw error;
  }
}

function cleanProblemListData(data) {
  const mainCategories = [];
  const subCategories = {};

  data.rows.forEach(row => {
      const docId = row._id;
      const title = row.title;
      mainCategories.push({ value: docId, lable: title });

      row.dag.forEach(dagItem => {
          if (!subCategories[docId]) {
              subCategories[docId] = [];
          }
          subCategories[docId].push({ lable: dagItem.title, value: dagItem.pids });
      });
  });

  return { mainCategories, subCategories };
}
