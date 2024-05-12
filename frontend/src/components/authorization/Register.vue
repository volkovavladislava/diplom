<template>
    <div class="col-md-5">
        <h4 class="mx-auto mt-4">Регистрация пользователя</h4>
        <form name="form" @submit="handleRegister">
            <div v-if="!successful">
                <div class="form-group">
                    <input type="text" class="form-control" name="name" placeholder="ФИО" v-model="user.name" required/>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="login" placeholder="Логин" v-model="user.login" required/>
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" name="password" placeholder="Пароль" v-model="user.password" required/>
                </div>
                <div class="form-group">
                    <input type="number" class="form-control" name="height" placeholder="Рост" v-model="user.height" required/>
                </div>
                <div class="form-group">
                    <input type="number" class="form-control" name="weight" placeholder="Вес" v-model="user.weight" required/>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="date_birth" placeholder="Дата рождения" v-model="user.date_birth" required/>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="gender" placeholder="Пол" v-model="user.gender" required/>
                </div>
                <div class="form-group">
                    <button class="btn btn-primary">Зарегистрировать</button>
                </div>
            </div>
            <div class="form-group">
                <div v-if="message" class="alert alert-danger" role="alert">{{message}}</div>
            </div>
        </form>
    </div>
</template>
<script>
    export default {
        name: 'RegisterUser',
        data() {
            return {
                user: {
                    name: "",
                    login: "",
                    password: "",
                    height: "",
                    weight: "",
                    date_birth: "",
                    gender: ""
                },
                successful: false,
                message: ''
            };
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        mounted() {
            if (this.loggedIn) {
                // Авторизация прошла успешно, переходим к главной странице.
                // Используем такую конструкцию, а не this.$router.push, так как требуется перезагрузить страницу для обновления локального хранилища
                window.location.href = '/';
            }
        },
        methods: {
            handleRegister(e) {
                e.preventDefault();
                this.message = '';

                this.$store.dispatch("auth/register", this.user) // обращаемся к методу register, который определён в auth.service.js
                    .then( data => {
                        this.message = data.message;
                        this.successful = true;
                    })
                    .catch(e => {
                            this.message = e.response.data.message;
                        }
                    );

            }
        }
    };
</script>