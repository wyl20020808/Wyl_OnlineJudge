<template>
    <div class="all">
        <BaseInfoComponentVue />
    </div>
</template>
    
<script>
import BaseInfoComponentVue from "@/components/component/User/UserHomePage/UserInfoCardComponent.vue";
export default {
    components:{
        BaseInfoComponentVue
    }
}
</script>

<style scoped>
.all{
    height: 1000px;;
    background-color: rgb(180, 207, 234);
}
</style>
