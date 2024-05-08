import { createWebHistory, createRouter } from "vue-router";
// импорт компонентов
import ListSystemMarks from "./components/marks/ListSystemMarks";
import AddMark from "./components/marks/AddMark";
import UserProfile from "./components/user/UserProfile";
import ListPersonalMarks from "./components/personalmark/ListPersonalMark"; 
import AddPersonalMark from "./components/personalmark/AddPersonalMark";
import DetailedPersonalMark from "./components/personalmark/DetailedPersonalMark";
import AddRecordPersonalMark from "./components/personalmark/AddRecordPersonalMark";

// определяем маршруты
const routes = [
    {
        path: "/listSystemMarks", // указание маршрута, по которому будем переходить к списку пользователей
        name: "listSystemMarks", // имя маршрута
        alias: "/listSystemMarks", // указание дополнительного маршрута
        component: ListSystemMarks, // компонент, на основании которого будет отрисовываться страница
        meta: {
            title: "Список показателей"
        }
    },{
        path: "/profile", 
        name: "profile", 
        alias: "/profile", 
        component: UserProfile,
        meta: {
            title: "Профиль"
        }
    },{
        path: "/addRecordMark/:id", 
        name: "addRecordMark", 
        alias: "/addRecordMark", 
        component: AddMark,
        props: true, 
        meta: {
            title: "Добавление записи по показателю"
        }
    },{
        path: "/listPersonalMarks", // указание маршрута, по которому будем переходить к списку пользователей
        name: "listPersonalMarks", // имя маршрута
        alias: "/listPersonalMarks", // указание дополнительного маршрута
        component: ListPersonalMarks, // компонент, на основании которого будет отрисовываться страница
        meta: {
            title: "Список собственных показателей"
        }
    },
    {
        path: "/addPersonalMark", // указание маршрута, по которому будем переходить к списку пользователей
        name: "addPersonalMark", // имя маршрута
        alias: "/addPersonalMark", // указание дополнительного маршрута
        component: AddPersonalMark, // компонент, на основании которого будет отрисовываться страница
        meta: {
            title: "Добавление собственного показателя"
        }
    },
    {
        path: "/detailedPersonalMark/:id", // указание маршрута, по которому будем переходить к списку пользователей
        name: "detailedPersonalMark", // имя маршрута
        alias: "/detailedPersonalMark", // указание дополнительного маршрута
        component: DetailedPersonalMark, // компонент, на основании которого будет отрисовываться страница
        props: true,
        meta: {
            title: "Статистика собственного показателя"
        }
    },
    {
        path: "/addRecordPersonalMark/:id", // указание маршрута, по которому будем переходить к списку пользователей
        name: "addRecordPersonalMark", // имя маршрута
        alias: "/addRecordPersonalMark", // указание дополнительного маршрута
        component: AddRecordPersonalMark, // компонент, на основании которого будет отрисовываться страница
        props: true,
        meta: {
            title: "Добавление значения собственного показателя"
        }
    },
    
];

const router = createRouter({
    history: createWebHistory(), // указываем, что будет создаваться история посещений веб-страниц
    routes, // подключаем маршрутизацию
});

// указание заголовка компонентам (тега title), заголовки определены в meta
router.beforeEach((to, from, next) => {
    // для тех маршрутов, для которых не определены компоненты, подключается только App.vue
    // поэтому устанавливаем заголовком по умолчанию название "Главная страница"
    document.title = to.meta.title || 'Главная страница';
    next();
});

export default router;