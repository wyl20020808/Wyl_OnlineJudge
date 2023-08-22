<template>
  <div class="main-container">
    <ProblemCreateComponent />
    <ProblemListViewVue />
  </div>
</template>

<script>
import ProblemCreateComponent from '@/components/component/problem/ProblemCreateComponent.vue';
import ProblemListViewVue from '@/components/component/problem/ProblemListView.vue';
export default {
  components:{
    ProblemCreateComponent,
    ProblemListViewVue
  },
 

};
</script>

<style>


</style>

