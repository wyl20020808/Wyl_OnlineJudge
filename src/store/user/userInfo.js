import axios from "axios";
import router from "@/router/router";
const ModuleUserInfo = ({
  state: {
    userid:"",
    username:"",
    userimg:"",
    userpassword:"",
    userphone:"",
    useraddress:"",
    userloginstate:false,
    userisadmin:false,
  },
  getters: {

  },
  mutations: {
    updateLoginState(state,loginState){
        state.userloginstate = loginState;
    },
    updateForLogin(state,userinfo){
      state.userinfo = userinfo.userid;
      state.username = userinfo.username;
      state.userloginstate = true;
    }
  },
  actions: {
    signin(context,userinfo){
        axios.post('http://localhost:8088/user/signin', userinfo,)
        .then(response => {
          if(response.data === "success") {
            alert("注册成功！")
          }else{
            alert("注册失败！" + response.data)
          }          
        })
        .catch(error => {
          alert("注册失败！" + error.data)
        });
    },
    login(context,userinfo){
      axios.post('http://localhost:8088/user/login', userinfo,)//这里注意不能用get，get有别的用法
        .then(response => {
          if(response.data > 0) {
            alert("欢迎回来！" + userinfo.username)
            userinfo.userid = response.data;
            context.commit('updateForLogin',userinfo)
            router.push({name:'home'})
          }else if(response.data === 0){
            alert("抱歉，您输入的密码有误！" )
          }else if(response.data === -1){
            alert("服务器异常，请稍后再试")
          }else{
            alert("抱歉，您输入的密码不正确")
          }
        })
        .catch(error => {
          alert("服务器异常，请稍后再试" + error.data)
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