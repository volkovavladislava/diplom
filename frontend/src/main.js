import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // Маршрутизация
import 'bootstrap/dist/css/bootstrap.css'// Подключение Bootstrap
// import 'bootstrap/dist/js/bootstrap.min.js'
import store from './store';

const app = createApp(App); // Создание экземпляра приложения
app.use(router); 
app.use(store);
app.mount('#app'); // Привязка экземпляра приложения к странице HTML (находится в public)
