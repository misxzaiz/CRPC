import {
	createSSRApp
} from "vue";
import App from "./App.vue";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css';
export function createApp() {
	const app = createSSRApp(App);
	app.use(ElementPlus)
	app.use(VXETable);
	return {
		app,
	};
}
