<template>
    <div class="container">
        <div  class="labelm" >
             <div class="row justify-content-md-center">
                <h4 class="text-center fs-4 col-md-5 labelm">Вход в систему</h4>
             </div>
             <div class="row justify-content-md-center ">
                <div  class="col-md-5 ">
                    <form name="form" @submit="handleLogin">
                        <div class="mb-3">
                            <input type="text" class="form-control" name="login" placeholder="Логин" v-model="user.login" required/>
                        </div>
                        <div class="mb-3">
                            <input type="password" class="form-control" name="password" placeholder="Пароль" v-model="user.password" required/>
                        </div>
                        <div class="mb-3">
                            <button class="btn btn-primary" :disabled="loading">
                                <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                                <span>Войти</span>
                            </button>
                        </div>
                        <router-link to="/register">
                            Зарегистрироваться
                        </router-link>
                        <div class="mb-3">
                            <div v-if="message" class="alert alert-danger" role="alert">{{message}}</div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'LoginUser',
        data() {
            return {
                user: {
                    name: "",
                    login: "",
                    password: ""
                },
                loading: false,
                message: ''
            };
        },
        computed: { // вычисляемые свойства
            loggedIn() {
                return this.$store.state.auth.status.loggedIn; // $store - локальное хранилище
            }
        },
        created() {
            if (this.loggedIn) {
                // Авторизация прошла успешно, переходим к главной странице.
                // Используем такую конструкцию, а не this.$router.push, так как требуется перезагрузить страницу для обновления локального хранилища
                window.location.href = '/';
            }
        },
        methods: {
            handleLogin(e) {
                e.preventDefault();
                this.loading = true;
                this.$store.dispatch("auth/login", this.user) // обращаемся к методу login, который определён в auth.service.js
                    .then(() => {
                        window.location.href = '/'; // авторизация прошла успешно, переходим к главной странице. Используем такую конструкцию, а не this.$router.push, так как требуется перезагрузить страницу для обновления локального хранилища
                    })
                    .catch(e => {
                            this.loading = false;
                            this.message = e.response.data.message;
                        }
                    );
            }
        }
    };
</script>