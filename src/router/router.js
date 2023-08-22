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
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
