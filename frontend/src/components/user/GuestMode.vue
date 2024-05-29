<template>
    <div class="container">
        <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля должны быть обязательно заполнены</p>

        <div class="labelm">
            <div class="row justify-content-md-center labelm">
                <div  class="col-md-5 labelm">
                    <div class="mb-3 labelm"  >
                        Введите логин и пароль для входа в режиме гостевого доступа
                    </div>
                    <div class="mb-3 labelm">
                        <label for="inputName" class="form-label">Введите логин </label>
                        <input type="text" class="form-control" id="inputName"  v-model="login">
                    </div>
                    <div class="mb-3 labelm">
                        <label for="inputName" class="form-label">Введите пароль </label>
                        <input type="text" class="form-control" id="inputName"  v-model="password">
                    </div>
                    
                    <button type="button" class="btn btn-outline-success " @click="loginGuest()">Войти</button>
                    <div class="alert alert-danger" role="alert" v-if="showAlert" style="margin-top: 20px">
                        Неверно введен логин или пароль
                    </div>
                    <div class="alert alert-danger" role="alert" v-if="showAlert1" style="margin-top: 20px">
                        Временный пароль устарел. Требуется сгенерировать новый
                    </div>
                </div>
            </div>
            
        </div>


    </div>
</template>

<script setup>
import { ref  } from 'vue';
import http from "../../http-common";
import { useRouter } from 'vue-router';

    const router = useRouter();

    const login= ref(null)
    const password= ref(null)

    const isFormValid = ref(true);
    const showAlert = ref(false);
    const showAlert1 = ref(false);

    async function loginGuest() {
        if (login.value && password.value) {
            isFormValid.value = true;
            try {
                var data = {
                    login: login.value,
                    password: password.value
                };
                
                const response = await http.post('/loginGuest' , data);

                const encodedData = encodeURIComponent(JSON.stringify(response.data));
                router.push({ path: '/guestListStatistic' , query: { data: encodedData } });


            } catch (error) {

                if(error.response.status == 401 || error.response.status == 404){
                    showAlert.value = true
                    setTimeout(() => {
                        showAlert.value = false;
                    }, 3000);
                }

                if(error.response.status == 403){
                    showAlert1.value = true
                    setTimeout(() => {
                        showAlert1.value = false;
                    }, 3000);
                }

                
                // console.log(error)
            }
        } else {isFormValid.value = false;}
    }
    

</script>