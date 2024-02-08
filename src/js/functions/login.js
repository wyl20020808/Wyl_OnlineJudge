import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import { createVNode } from "vue";
import { Modal } from "ant-design-vue";
import router from "@/router/router";
export function showLogin() {
    console.log("Show Login");
    Modal.confirm({
        title: "您未登录",
        icon: createVNode(ExclamationCircleOutlined),
        content: "请问是否需要跳转到登录页面进行登录？",
        onOk() {
            router.push({ path: "/" + "userlogin" });
        },
        // eslint-disable-next-line @typescript-eslint/no-empty-function
        onCancel() { },
    });
}
export const isLogin = localStorage.getItem("user") !== "null";