// store/index.js
import { createStore } from 'vuex'
import ModuleUserInfo from './user/userInfo'
import { ElNotification } from 'element-plus'
import axios from 'axios';
import { SERVER_URL } from '../js/functions/config';
import { sleep } from "@/js/functions/TimeAbout";
import { useStore } from 'vuex';

const store = useStore();
export default createStore({
  state: {
    unRead: 0, // 添加未读消息状态
    newMessage: new Map(),
  },
  getters: {
    unRead: state => state.unRead, // 添加 getter
  },
  mutations: {
    ADD_UNREAD(state, cnt) { // 添加 mutation
      state.unRead += cnt;
    },
    SET_UNREAD(state, cnt) { //
      state.unRead = cnt;
    },
    setNewMessage(state, { key, value }) {
      state.newMessage.set(key, value);
    }
  },
  actions: {
    updateNewMessage({ commit }, payload) {
      commit('setNewMessage', payload);
    },
    async signin(context, userinfo) {

      await axios.post(`${SERVER_URL}/user/signin`, userinfo,)
        .then(async response => {
          // alert("yes")
          let type = 'error';
          if (response.data === '注册成功') {
            type = "success";
            await axios.post(`${SERVER_URL}/userextra/synchronizeinfo `, {})//注册完同步一下表的信息
          }
          context.dispatch("notice", {
            title: response.data,
            message: "",
            type: type,
          })
          if (type === 'success') {
            router.push({ name: 'userlogin' })
          }
        })
        .catch(error => {
          context.dispatch("notice", {
            title: error,
            message: "",
            type: 'error',
          })
        });
    },
    async addUnreadCnt(context,cnt){
      const user = localStorage.getItem('user');
      user.unread += cnt;
      localStorage.setItem('user', JSON.stringify(user));
    },
    async SynchronizeInfo(context, info) {
      try {
        const response = await axios.post(`${SERVER_URL}/user/query`, info.userinfo);
        response.data.userloginstate = info.loginState;
        localStorage.setItem('user', JSON.stringify(response.data));//同步本地数据
        // console.log(response.data);
        await axios.post(`${SERVER_URL}/user/synchronize/userinfo`, response.data);
        context.dispatch("notice", {
          title: 'Success',
          message: "数据同步成功！ ",
          type: 'success',
        });
        context.dispatch("notice", {
          title: "退出成功！",
          message: "再见！" + userinfo.username,
          type: "success",
        });
        localStorage.setItem('user', null);//同步本地数据
      } catch (error) {
        // context.dispatch("notice", {
        //   title: 'Error',
        //   message: "服务器后端有异常！ " + error,
        //   type: 'error',
        // });
      }
    },
    async SynchronizeInfo_login(context, info) {
      try {
        console.log("ues")
        const response = await axios.post(`${SERVER_URL}/user/query`, info.userinfo);
       
        response.data.userloginstate = info.loginState;
    
        localStorage.setItem('user', JSON.stringify(response.data));//同步本地数据
        
        console.log(JSON.parse(localStorage.getItem('user')));
        // console.log(response.data);
        // await axios.post(`${SERVER_URL}/user/synchronize/userinfo`, response.data);
        context.dispatch("notice", {
          title: 'Success',
          message: "数据同步成功！ ",
          type: 'success',
        });
        // context.dispatch("notice", {
        //   title: "退出成功！",
        //   message: "再见！" + userinfo.username,
        //   type: "success",
        // });
        // localStorage.setItem('user', null);//同步本地数据
      } catch (error) {
        // context.dispatch("notice", {
        //   title: 'Error',
        //   message: "服务器后端有异常！ " + error,
        //   type: 'error',
        // });
      }
    },
    async login(context, userinfo) {
      axios.post(`${SERVER_URL}/user/login`, userinfo, {
        headers: {
          'Authorization': `login`
        }
      })//这里注意不能用get，get有别的用法
        .then(response => {
          if (!["passworderror", "backenderror", "usernotexist"].includes(response.data)) {
            // alert("window.location.href = '/';window.location.href = '/';" + userinfo.username)
            context.dispatch("notice", {
              title: '欢迎回来！',
              message: userinfo.username,
              type: 'success',
            })
            userinfo.userid = response.data.split(" ")[1];//空格后面的是id
            response.data = response.data.split(" ")[0]
            localStorage.setItem("token",response.data);
           context.dispatch("SynchronizeInfo_login", {
              userinfo,
              loginState: "true"
            }
            )
            // console.log(response.data,"abcd")
            // window.location.href = '/';
           
            sleep(100).then(() => {
              window.location.href = '/';
            
              // if (document.referrer) {
              //   window.location.href = document.referrer;
              // } else {
              //   // 如果没有referrer信息，可以选择回到首页或其他页面
              //   window.location.href = '/';
              // }
            })

            // let user = JSON.parse(localStorage.getItem('user'));
            // console.log(user)
            // router.push({name:'home'})
          } else if (response.data === "passworderror") {
            // alert("抱歉，您输入的密码有误！" )
            context.dispatch("notice", {
              title: 'Error',
              message: "密码错误！",
              type: 'error',
            })
          } else if (response.data === "backenderror") {
            // alert("服务器异常，请稍后再试")
            context.dispatch("notice", {
              title: 'Error',
              message: "服务器异常！",
              type: 'error',
            })
          } else {
            // alert("抱歉，您的用户名不存在！")
            context.dispatch("notice", {
              title: 'Error',
              message: "用户名不存在！",
              type: 'error',
            })
          }
        })
        .catch(error => {
          // context.dispatch("notice",{
          //   title: 'Error',
          //   message: "服务器异常！ " + error ,
          //   type: 'error',
          // })
        });
    },
    notice(context, noticeInfo) {
      ElNotification({
        title: noticeInfo.title,
        message: noticeInfo.message,
        type: noticeInfo.type,
      })
    },
    addUnRead({ commit }, cnt) { // 添加 action
      commit('ADD_UNREAD', cnt);
    },
    setUnRead({ commit }, cnt){
        commit('SET_UNREAD', cnt);
    }
  },
  getters: {
    getNewMessage: (state) => state.newMessage
  },
  modules: {
    userInfo: ModuleUserInfo,
  }
});
