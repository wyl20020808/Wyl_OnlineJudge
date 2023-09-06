import { createRouter,  createWebHistory } from 'vue-router'
import MainView from "../components/pages/MainView.vue"
import CompetitionView from "../components/pages/CompetitionView.vue"
import EvaluationQueueView from "../components/pages/EvaluationQueueView.vue"
import ProblemsView from "../components/pages/Problem/ProblemsView.vue"
import RankListView from "../components/pages/RankListView.vue"
import TissueView from "../components/pages/TissueView.vue"
import UserInfoView from "../components/pages/UserInfoView.vue"
import CreateProblemView from "../components/pages/Problem/CreateProblemView.vue"
import ProblemDetailView from "../components/pages/Problem/ProblemDetailView.vue"
import UserRegitserView from "../components/pages/User/UserRegisterView.vue"
import UserLoginView from "../components/pages/User/UserLoginView.vue"
import UserHomeView from '@/components/pages/User/UserHome/UserHomeView.vue'
import ProblemEditView from '@/components/pages/Problem/ProblemEditView.vue'
import SubmitCodeView from "../components/pages/Problem/SubmitCodeView.vue"
import JudgeContentView from "@/components/pages/judge/JudgeContentView.vue"
import CreateContestView from "@/components/pages/contest/CreateContestView.vue"
const routes = [
  {
    path: '/',
    name: 'home',
    component: MainView
  },
  {
    path: '/competition/',
    name: 'competition',
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
    props:true,
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
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { top: 0 }
  },
})

export default router
