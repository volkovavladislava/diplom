<template>
<div class="container" >

     <div  class="labelm" >
        <div class="row justify-content-md-center">
            <h4 class="text-center fs-4 col-md-5 labelm">Регистрация пользователя</h4>
        </div>
        <div class="row justify-content-md-center ">
            <div  class="col-md-7 ">
                <form name="form" @submit="handleRegister">
                    <div v-if="!successful" >
                        <div class="mb-3">
                            <input type="text" class="form-control" name="name" placeholder="ФИО" v-model="user.name" required/>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" name="login" placeholder="Логин" v-model="user.login" required/>
                        </div>
                        <div class="mb-3">
                            <input type="password" class="form-control" name="password" placeholder="Пароль" v-model="user.password" required/>
                        </div>
                        <div class="mb-3">
                            <input type="number" class="form-control" name="height" placeholder="Рост" v-model="user.height" required/>
                        </div>
                        <div class="mb-3">
                            <input type="number" class="form-control" name="weight" placeholder="Вес" v-model="user.weight" required/>
                        </div>
                        <div class="mb-3">
                            <input type="date" class="form-control" name="date_birth" placeholder="Дата рождения" v-model="user.date_birth" required/>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" name="gender" placeholder="Пол: м/ж" v-model="user.gender" required/>
                        </div>
                        <div class="mb-3">
                            <button class="btn btn-outline-success">Зарегистрировать</button>
                        </div>
                    </div>
                    <div class="col-md-3">
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