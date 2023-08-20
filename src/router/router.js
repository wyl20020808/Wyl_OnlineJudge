import { createRouter,  createWebHistory } from 'vue-router'
import MainView from "../components/pages/MainView.vue"
import CompetitionView from "../components/pages/CompetitionView.vue"
import EvaluationQueueView from "../components/pages/EvaluationQueueView.vue"
import ProblemsView from "../components/pages/ProblemsView.vue"
import RankListView from "../components/pages/RankListView.vue"
import TissueView from "../components/pages/TissueView.vue"
import UserInfoView from "../components/pages/UserInfoView.vue"
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
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
