import {
	createSSRApp
} from "vue";
import App from "./App.vue";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
export function createApp() {
	const app = createSSRApp(App);
	app.use(ElementPlus)
	return {
		app,
	};
}

// export const MAIN_URI = "http://127.0.0.1:8000"

export const MAIN_URI = "https://dominant-ant-formerly.ngrok-free.app"