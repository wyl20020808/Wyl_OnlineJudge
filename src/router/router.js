import { createRouter, createWebHistory } from 'vue-router'
import MainView from "../components/pages/MainView.vue"
import CompetitionView from "../components/pages/CompetitionView.vue"
import EvaluationQueueView from "../components/pages/EvaluationQueueView.vue"
import DicussView from "../components/pages/DicussView.vue"
import ProblemsView from "../components/pages/Problem/ProblemsView.vue"
import RankListView from "../components/pages/RankListView.vue"
import TissueView from "../components/pages/TissueView.vue"
import UserInfoView from "../components/pages/UserInfoView.vue"
import CreateProblemView from "../components/pages/Problem/CreateProblemView.vue"
import ProblemDetailView from "../components/pages/Problem/ProblemDetailView.vue"
import UserRegitserView from "../components/pages/User/UserRegisterView.vue"
import UserLoginView from "../components/pages/User/UserLoginView.vue"
import CreateDiscussView from "../components/pages/discuss/CreateDiscussView.vue"
import DiscussContentView from "../components/pages/discuss/DiscussContentView.vue"
import UserHomeView from '@/components/pages/User/UserHome/UserHomeView.vue'
import ProblemEditView from '@/components/pages/Problem/ProblemEditView.vue'
import SubmitCodeView from "../components/pages/Problem/SubmitCodeView.vue"
import JudgeContentView from "@/components/pages/judge/JudgeContentView.vue"
import CreateContestView from "@/components/pages/contest/CreateContestView.vue"
import ContestView from "@/components/pages/contest/ContestView.vue"
import ProblemContentView2 from "@/components/component/problem/ProblemContentView2.vue"
import ContestProblemView from "@/components/pages/contest/ContestProblemView.vue"
import ContestSubmitView from "../components/pages/contest/ContestSubmitView.vue"
import MessageView from "@/components/pages/message/MessageView.vue"
import codeEditorComponent from "@/components/component/code/codeEditorComponent"
import UserSettingView from "../components/pages/User/UserSettingView.vue"
import UserCollectSetView from "../components/pages/User/UserCollectSetView.vue"
import YozoFile from "@/components/component/YozoFile/YozoFile.vue"
const routes = [
  {
    path: '/yozoFile/',
    name: 'yozoFile',
    component: YozoFile
  },
  {
    path: '/',
    name: 'home',
    component: MainView
  },
  {
    path: '/code/',
    name: 'code',
    component: codeEditorComponent
  },
  {
    path: '/contest/',
    name: 'contest',
    component: CompetitionView
  },
  {
    path: '/evaluationqueue/',
    name: 'evaluationqueue',
    component: EvaluationQueueView
  },
  {
    path: '/problems/',
    name: 'problems',
    component: ProblemsView
  },
  {
    path: '/ranklist/',
    name: 'ranklist',
    component: RankListView
  },
  {
    path: '/tissue/',
    name: 'tissue',
    component: TissueView
  },
  {
    path: '/userinfo/',
    name: 'userinfo',
    component: UserInfoView
  },
  {
    path: '/createproblem/',
    name: 'createproblem',
    component: CreateProblemView
  },
  {
    path: '/problemdetail/',
    name: 'problemdetail',
    component: ProblemDetailView,
    props: "true"
  },
  {
    path: '/userregitser/',
    name: 'userregitser',
    component: UserRegitserView,
  },
  {
    path: '/userlogin/',
    name: 'userlogin',
    component: UserLoginView,
  },
  {
    path: '/userhome/',
    name: 'userhome',
    component: UserHomeView,
  },
  {
    path: '/problemedit/:problemid',
    name: 'problemedit',
    component: ProblemEditView,
    props: true,
  },
  {
    path: '/problem/submit',
    name: 'submitcode',
    component: SubmitCodeView,
  },
  {
    path: '/judge/judgecontent',
    name: 'judgecontent',
    component: JudgeContentView,
  },
  {
    path: '/contest/create',
    name: 'createcontest',
    component: CreateContestView,
  },
  {
    path: '/contest/content',
    name: 'contestcontest',
    component: ContestView,
  },
  {
    path: '/problem',
    name: 'problem',
    component: ProblemContentView2,
  },
  {
    path: '/contest/problem',
    name: 'contestproblem',
    component: ContestProblemView,
  },
  {
    path: '/contest/submit',
    name: 'contestsubmit',
    component: ContestSubmitView,
  },
  {
    path: '/message',
    name: 'message',
    component: MessageView,
  },
  {
    path: '/discuss',
    name: 'discuss',
    component: DicussView,
  },
  {
    path: '/creatediscuss',
    name: 'creatediscuss',
    component: CreateDiscussView,
  },
  {
    path: '/discusscontent',
    name: 'discusscontent',
    component: DiscussContentView,
  },
  {
    path: '/usersetting',
    name: 'usersetting',
    component: UserSettingView,
  },
  {
    path: '/collectset',
    name: 'collectset',
    component: UserCollectSetView,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { top: 0 }
  },
})

let fresh = false;
router.beforeEach(async (to, from, next) => {

    const shouldContinue = await checkIsLogin(to);
    
      next();
    
});

import axios from "axios"
import { SERVER_URL } from "../js/functions/config.js"
import { useStore } from 'vuex'
import { warningMessage } from '../js/functions/common.js'
import { notice } from '../js/functions/common.js'
import { sleep } from '../js/functions/TimeAbout.js'
async function checkIsLogin(to) {
  try {
    const res = await axios.post(`${SERVER_URL}/user/checkLogin`);
    if (res.data === false) {
      localStorage.setItem('user', null); // 同步本地数据
      if (!sessionStorage.getItem('refreshed')) {
        sessionStorage.setItem('refreshed', 'true');
        notice("",{
          title: '登陆已过期',
          message: "抱歉，您的登录信息已过期，请重新登陆 ",
          type: 'error',
        })
        sleep(1000).then(function () {
          window.location.href = `${to.fullPath}`;
        })
        // 保存目标路径
        // 重定向到登录页面，并在登录成功后跳转到目标页面
        // window.location.href = `${to.fullPath}`;
        return false; // 表示不需要执行next()
      }
    } else {
      sessionStorage.removeItem('refreshed');
    }
    return true; // 用户已登录，或不需要刷新，继续执行next()
  } catch (error) {
    console.error("An error occurred:", error);
    return true; // 出现错误时，也继续执行next()
  }
}


export default router
