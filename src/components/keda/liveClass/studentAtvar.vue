<template>
  <a-row style="min-height: 250px;" :key="studentName" class="avatar-trophy">
    <a-row >
      <img :src="images[idx]" alt="Avatar"  class="avatar image-avatar" :class="`avatar-${idx}`"/>
    </a-row>
    <a-row 
      style="font-size: 18px; font-weight: bold; border-bottom: 1px solid #ccc"
      class="centered-text "
    >
      {{ studentName }}
    </a-row>
    <a-row style="margin-top: 10px">
      <a-col style="margin-right: 5px; position: absolute; left: 35px">
        <img
          src="./static/奖杯.png"
          @click="giveTrophy(studentName, idx)"
          alt="赠送奖杯"
          style="width: 40px; height: 40px; cursor: pointer"
        />
      </a-col>
      <a-col style="margin-left: 40px; color: orange" class="centered-text">
        {{ trophyCount }}
      </a-col>
    </a-row>

    <div v-if="showTrophy[idx]" :class="`trophy trophy-${idx}`" ref="trophy" style="z-index:1000">
      <img :src="trophyImageUrl" alt="Trophy" class="trophy-image" />
    </div>
  </a-row>
</template>

<script setup>
import { ref, nextTick, defineProps, defineEmits, onMounted } from "vue";
import trophyImageUrl from './static/奖杯.png'; // Update with the generated trophy image URL

const emit = defineEmits();//调用父组件函数

const images = ref([
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\1.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\2.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\3.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\4.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\5.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\6.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\7.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\8.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\9.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\10.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\11.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\components\\keda\\liveClass\\static\\12.jpg`),
  require(`E:\\Desktop\\test\\keda.ac\\src\\assets\\static\\pictures\\wyl.jpg`)
]);

const basePath = "";

onMounted(async() => {

});

const props = defineProps({
  studentName: String, // 定义从父组件接收的学生姓名参数
  trophyCount: Number, // 定义从父组件接收的奖杯数量参数
  idx: Number,
});

const showTrophy = ref(new Array(images.value.length).fill(false));

const giveTrophy = async (studentName, idx) => {
  console.log(studentName);
  emit("trophyGiven", studentName); //加奖杯

  const audio = new Audio(require('./static/奖杯奖励音效.mp3'));
  audio.play();

  showTrophy.value[idx] = true; // Show the trophy before starting the animation

  await nextTick(); // Wait for the DOM to update

  const avatar = document.querySelector(`.avatar-${idx}`);
  const trophyElement = document.querySelector(`.trophy-${idx}`);

  // Reset the animation by removing and adding the class
  trophyElement.classList.remove('trophy-animation');
  void trophyElement.offsetWidth; // Trigger reflow to reset the animation
  trophyElement.classList.add('trophy-animation');

  const windowHeight = window.innerHeight;
  const windowWidth = window.innerWidth;

  trophyElement.style.top = `calc(${windowHeight / 2}px - 25px)`;
  trophyElement.style.left = `calc(${windowWidth / 2}px - 25px)`;

  const avatarRect = avatar.getBoundingClientRect();
  const trophyRect = trophyElement.getBoundingClientRect();

  const deltaX = avatarRect.left + avatarRect.width / 2 - trophyRect.left - trophyRect.width / 2;
  const deltaY = avatarRect.top + avatarRect.height / 2 - trophyRect.top - trophyRect.height / 2;

  trophyElement.style.setProperty("--deltaX", `${deltaX}px`);
  trophyElement.style.setProperty("--deltaY", `${deltaY}px`);

  setTimeout(() => {
    showTrophy.value[idx] = false;
  }, 3000); // 动画结束后隐藏奖杯
};
</script>

<style scoped>
.avatar-trophy {
  border: 1px solid #ccc;
  padding: 30px;		
  margin-bottom: 20px;
  border-radius: 5px;
  background-color: #f5f5f5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 10px;
}

.centered-text {
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: "Microsoft YaHei", sans-serif;
}

.trophy {
  width: 50px;
  height: 50px;
  position: fixed; /* Use fixed position to center in the whole page */
  display: none; /* Initially hidden */
}

.trophy-image {
  width: 100%;
  height: 100%;
}

.trophy-animation {
  display: block; /* Show the trophy when animation starts */
  animation: trophy-flying 1s forwards;
}

@keyframes trophy-flying {
  0% {
    transform: translateY(0) scale(4) rotate(0deg);
    opacity: 1;
  }
  100% {
    transform: translate(var(--deltaX), var(--deltaY)) scale(1) rotate(360deg);
    opacity: 0;
  }
}


.image-avatar:hover {
  transform: scale(1.3);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>