import axios from "axios";
import router from "@/router/router";
import { ElNotification } from 'element-plus'
import { sleep } from "@/js/functions/TimeAbout";
import {SERVER_URL,SERVER} from "../../js/functions/config"
const ModuleUserInfo = ({
  state: {
    userid:"",
    username:"",
    userimg:"",
    userpassword:"",
    userphone:"",
    useraddress:"",
    userloginstate:"false",
    userisadmin:"false",
    userpicture:"",
  },
  getters: {

  },
  mutations: {

  },
  actions: {
    notice(context,noticeInfo)  {
        ElNotification({
            title: noticeInfo.title,
            message: noticeInfo.message,
            type: noticeInfo.type,
        })
      },
    signin(context,userinfo){
        axios.post(`${SERVER_URL}/user/signin`, userinfo,)
        .then(response => {
          // alert("yes")
          let type = 'error';
          if(response.data === '注册成功'){
              type = "success";
          }
          context.dispatch("notice",{
            title:response.data,
            message:"",
            type:type,
          })   
          if(type === 'success'){
            router.push({name:'userlogin'})
          }       
        })
        .catch(error => {
          context.dispatch("notice",{
            title:error,
            message:"",
            type:'error',
          }) 
        });
    },
    async SynchronizeInfo(context, info) {
      try {
        const response = await axios.post(`${SERVER_URL}/user/query`, info.userinfo);
        response.data.userloginstate = info.loginState;
        localStorage.setItem('user', JSON.stringify(response.data));//同步本地数据
        await axios.post(`${SERVER_URL}/user/synchronize/userinfo`, response.data);
        context.dispatch("notice", {
          title: 'Success',
          message: "数据同步成功！ ",
          type: 'success',
        });
      } catch (error) {
        context.dispatch("notice", {
          title: 'Error',
          message: "服务器异常！ " + error,
          type: 'error',
        });
      }
    },
    login(context,userinfo){
      axios.post(`${SERVER_URL}/user/login`, userinfo,)//这里注意不能用get，get有别的用法
        .then(response => {
          if(response.data > 0) {
            // alert("欢迎回来！" + userinfo.username)
            context.dispatch("notice",{
              title: '欢迎回来！',
              message: userinfo.username,
              type: 'success',
            })
            userinfo.userid = response.data;
            // console.log(userinfo.username)
            context.dispatch("SynchronizeInfo",{
              userinfo,
              loginState:"true"
            }
             )
             sleep(500).then(()=>{
              window.location = `${SERVER}`;
            })
            
            // let user = JSON.parse(localStorage.getItem('user'));
            // console.log(user)
            // router.push({name:'home'})
          }else if(response.data === 0){
            // alert("抱歉，您输入的密码有误！" )
            context.dispatch("notice",{
              title: 'Error',
              message: "密码错误！",
              type: 'error',
            })
          }else if(response.data === -1){
            // alert("服务器异常，请稍后再试")
            context.dispatch("notice",{
              title: 'Error',
              message: "服务器异常！",
              type: 'error',
            })
          }else{
            // alert("抱歉，您的用户名不存在！")
            context.dispatch("notice",{
              title: 'Error',
              message: "用户名不存在！",
              type: 'error',
            })
          }
        })
        .catch(error => {
          context.dispatch("notice",{
            title: 'Error',
            message: "服务器异常！ " + error ,
            type: 'error',
          })
        });
    }
  },
  modules: {
    
  }
})
export default ModuleUserInfo;

// 在 Vuex 中，你可以通过 this.$store.commit 来调用 mutations，
// 通过 this.$store.dispatch 来调用 actions。
// 你也可以通过 this.$store.state.ModuleUserInfo 来访问 state，
// 通过 this.$store.getters 来访问 getters。