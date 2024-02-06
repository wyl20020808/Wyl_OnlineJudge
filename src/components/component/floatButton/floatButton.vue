<template>
  <a-popover v-model:open="visible" title="工具箱" trigger="click">
    <template #content>
      <a-row style="margin: 20px">
        <a-row >
          <a-col>
            <a-row style="align-items: center;cursor: pointer;" @click="jumpToYozoFile" class="highlight-on-hover">
              <a-col>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="25"
                  height="25"
                  fill="currentColor"
                  class="bi bi-file-earmark-pdf"
                  viewBox="0 0 16 16"
                >
                  <path
                    d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2zM9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5v2z"
                  />
                  <path
                    d="M4.603 14.087a.81.81 0 0 1-.438-.42c-.195-.388-.13-.776.08-1.102.198-.307.526-.568.897-.787a7.68 7.68 0 0 1 1.482-.645 19.697 19.697 0 0 0 1.062-2.227 7.269 7.269 0 0 1-.43-1.295c-.086-.4-.119-.796-.046-1.136.075-.354.274-.672.65-.823.192-.077.4-.12.602-.077a.7.7 0 0 1 .477.365c.088.164.12.356.127.538.007.188-.012.396-.047.614-.084.51-.27 1.134-.52 1.794a10.954 10.954 0 0 0 .98 1.686 5.753 5.753 0 0 1 1.334.05c.364.066.734.195.96.465.12.144.193.32.2.518.007.192-.047.382-.138.563a1.04 1.04 0 0 1-.354.416.856.856 0 0 1-.51.138c-.331-.014-.654-.196-.933-.417a5.712 5.712 0 0 1-.911-.95 11.651 11.651 0 0 0-1.997.406 11.307 11.307 0 0 1-1.02 1.51c-.292.35-.609.656-.927.787a.793.793 0 0 1-.58.029zm1.379-1.901c-.166.076-.32.156-.459.238-.328.194-.541.383-.647.547-.094.145-.096.25-.04.361.01.022.02.036.026.044a.266.266 0 0 0 .035-.012c.137-.056.355-.235.635-.572a8.18 8.18 0 0 0 .45-.606zm1.64-1.33a12.71 12.71 0 0 1 1.01-.193 11.744 11.744 0 0 1-.51-.858 20.801 20.801 0 0 1-.5 1.05zm2.446.45c.15.163.296.3.435.41.24.19.407.253.498.256a.107.107 0 0 0 .07-.015.307.307 0 0 0 .094-.125.436.436 0 0 0 .059-.2.095.095 0 0 0-.026-.063c-.052-.062-.2-.152-.518-.209a3.876 3.876 0 0 0-.612-.053zM8.078 7.8a6.7 6.7 0 0 0 .2-.828c.031-.188.043-.343.038-.465a.613.613 0 0 0-.032-.198.517.517 0 0 0-.145.04c-.087.035-.158.106-.196.283-.04.192-.03.469.046.822.024.111.054.227.09.346z"
                  />
                </svg>
              </a-col>
              <a-col> 文档编辑系统 </a-col>
            </a-row>
          </a-col>
          <a-col style="margin-left: 10px">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="25"
              height="25"
              fill="currentColor"
              class="bi bi-file-code"
              viewBox="0 0 16 16"
            >
              <path
                d="M6.646 5.646a.5.5 0 1 1 .708.708L5.707 8l1.647 1.646a.5.5 0 0 1-.708.708l-2-2a.5.5 0 0 1 0-.708l2-2zm2.708 0a.5.5 0 1 0-.708.708L10.293 8 8.646 9.646a.5.5 0 0 0 .708.708l2-2a.5.5 0 0 0 0-.708l-2-2z"
              />
              <path
                d="M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V2zm10-1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1z"
              />
            </svg>
          </a-col>
          <a-col> 贴代码工具 </a-col>
        </a-row>
      </a-row>
      <!-- 弹出内容 -->
      <a @click="hide" style="color: rgb(89, 138, 244)">关闭</a>
    </template>
    <template #default>
      <!-- 触发器内容 -->
      <div
        class="floating-ball"
        :class="{ 'is-near-edge': isNearEdge, left: isLeftEdge }"
        :style="{ left: `${position.x}px`, top: `${position.y}px` }"
        @mousedown="startDrag"
        @touchstart.prevent="startDrag"
        @mouseenter="onMouseEnter"
        @mouseleave="onMouseLeave"
        ref="ball"
      >
        <img
          style="width: 70px"
          src="@\assets\static\pictures\toolBox.png"
          alt=""
        />
        <!-- 悬浮球内容 -->
      </div>
    </template>
  </a-popover>
</template>

<script setup>
import { ref, onMounted } from "vue";
import router from "@/router/router";
//
const visible = ref(false);
const hide = () => {
  visible.value = false;
};
//

const position = ref({ x: 50, y: 600 });
const isDragging = ref(false);
const isNearEdge = ref(false);
const isLeftEdge = ref(true); // 新增变量，判断是否在左边缘
const edgeThreshold = 50; // 边缘阈值
const ballWidth = 50; // 悬浮球宽度
const hideOffset = 15; // 隐藏时的偏移量
const ball = ref(null); // 新增变量，引用悬浮球元素

//
const jumpToYozoFile = () => {
  router.push({ path: "/" + 'YozoFile' });
}
//

const onMouseEnter = () => {
  isNearEdge.value = false;
};

const onMouseLeave = () => {
  const screenWidth = window.innerWidth;
  if (position.value.x < edgeThreshold) {
    isNearEdge.value = true;
    isLeftEdge.value = true;
    position.value.x = -hideOffset; // 吸附到左边
  } else if (screenWidth - position.value.x - ballWidth < edgeThreshold) {
    isNearEdge.value = true;
    isLeftEdge.value = false;
    position.value.x = screenWidth - ballWidth; // 吸附到右边
  }
};

let currentDragEvent, currentStopEvent;

const startDrag = (event) => {
  event.preventDefault(); // 阻止默认行为，如选中文本
  console.log("startDrag: Starting drag");
  isDragging.value = true;
  const startX = event.type.includes("mouse")
    ? event.clientX
    : event.touches[0].clientX;
  const startY = event.type.includes("mouse")
    ? event.clientY
    : event.touches[0].clientY;
  const dragStartX = startX - position.value.x;
  const dragStartY = startY - position.value.y;

  const onDrag = (moveEvent) => {
    moveEvent.preventDefault(); // 阻止默认行为，如滚动
    const clientX = moveEvent.type.includes("mouse")
      ? moveEvent.clientX
      : moveEvent.touches[0].clientX;
    const clientY = moveEvent.type.includes("mouse")
      ? moveEvent.clientY
      : moveEvent.touches[0].clientY;
    position.value.x = clientX - dragStartX;
    position.value.y = clientY - dragStartY;
    isNearEdge.value = false;
    if (!isDragging.value) {
      console.log("onDrag: Dragging stopped, but onDrag is still being called");
      return; // 如果拖动已经停止，不应该再处理拖动事件
    }
    console.log("onDrag: Dragging at", moveEvent.clientX, moveEvent.clientY);
  };

  const stopDrag = () => {
    console.log("stopDrag: Stopping drag");
    isDragging.value = false;
    document.removeEventListener("mousemove", onDrag);
    document.removeEventListener("mouseup", stopDrag);
    document.removeEventListener("touchmove", onDrag);
    document.removeEventListener("touchend", stopDrag);
    if (!ball.value.contains(event.relatedTarget)) {
      onMouseLeave(); // 检查是否应该隐藏
    }
  };

  // 移除之前的事件监听器
  if (currentDragEvent) {
    document.removeEventListener("mousemove", currentDragEvent);
    document.removeEventListener("touchmove", currentDragEvent);
  }
  if (currentStopEvent) {
    document.removeEventListener("mouseup", currentStopEvent);
    document.removeEventListener("touchend", currentStopEvent);
  }

  // 添加新的事件监听器
  document.addEventListener("mousemove", onDrag);
  document.addEventListener("mouseup", stopDrag);
  document.addEventListener("touchmove", onDrag);
  document.addEventListener("touchend", stopDrag);

  // 保存当前的事件处理函数，以便后续移除
  currentDragEvent = onDrag;
  currentStopEvent = stopDrag;
};
</script>

<style scoped>
.floating-ball {
  position: fixed;
  width: 50px;
  height: 50px;

  cursor: pointer;
  z-index: 1000;
  transition: transform 0.3s ease-in-out;
}

.floating-ball.is-near-edge:not(.left) {
  transform: translateX(calc(100% - 25px)); /* 部分隐藏在右边 */
}

.floating-ball.is-near-edge.left {
  transform: translateX(calc(-100% + 25px)); /* 部分隐藏在左边 */
}

.highlight-on-hover:hover {
    color: #3798ec; /* 这里的颜色是示例，你可以根据需要更改 */
  }
</style>