import AuthService from '../services/auth.service';
const user = JSON.parse(localStorage.getItem('user'));
const initialState = user // состояния: авторизованный или неавторизованный пользователь
    ? { status: { loggedIn: true }, user }
    : { status: { loggedIn: false }, user: null };
export const auth = {
    namespaced: true,
    state: initialState,
    actions: { // действия: 1 - вход (login), 2 - выход (logout), 3 - регистрация (register)
        login({ commit }, user) {
            return AuthService.login(user).then(
                user => {
                    commit('loginSuccess', user);
                    return Promise.resolve(user);
                },
                error => {
                    commit('loginFailure');
                    return Promise.reject(error);
                }
            );
        },
        logout({ commit }) {
            AuthService.logout();
            commit('logout');
        },
        register({ commit }, user) {
            return AuthService.register(user).then(
                response => {
                    commit('registerSuccess');
                    // Promise - объект, представляющий результат успешного или неудачного завершения операции.
                    return Promise.resolve(response.data);
                },
                error => {
                    commit('registerFailure');
                    return Promise.reject(error);
                }
            );
        }
    },
    mutations: { // Мутации. Позволяют изменять состояние хранилища во Vuex. Сохраняют данные пользователя в локальное хранилище в браузере. Также удаляют данные пользователя
        loginSuccess(state, user) {
            state.status.loggedIn = true;
            state.user = user;
        },
        loginFailure(state) {
            state.status.loggedIn = false;
            state.user = null;
        },
        logout(state) {
            state.status.loggedIn = false;
            state.user = null;
        },
        registerSuccess(state) {
            state.status.loggedIn = false;
        },
        registerFailure(state) {
            state.status.loggedIn = false;
        }
    }
};