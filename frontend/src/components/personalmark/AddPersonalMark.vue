<template>
    <div class="container">
        <p v-if="!isFormValid" style="color: #eb4034;" class="labelm">Поля должны быть обязательно заполнены</p>

        <div class="labelm">
            <div class="row justify-content-md-center labelm">
                <div  class="col-md-5 labelm">
                    <div class="mb-3 labelm">
                        <label for="inputName" class="form-label">Введите название собственного показателя</label>
                        <input type="text" class="form-control" id="inputName"  v-model="name">
                    </div>
                    
                    <button type="button" class="btn btn-outline-success " @click="addPersonalMark()">Добавить данные</button>
                    <div class="alert alert-success" role="alert" v-if="showAlert">
                        Успешно!
                    </div>
                </div>
            </div>
            
        </div>


    </div>
</template>

<script setup>
import { ref, computed  } from 'vue';
import http from "../../http-common";
import { useStore } from 'vuex';
    
    const store = useStore();
    const currentUser = computed(() => store.state.auth.user);

    const isFormValid = ref(true);
    const showAlert = ref(false);

    const name= ref(null)
    // const userId = ref(1)

    async function addPersonalMark() {
        if (name.value) {
            isFormValid.value = true;
            try {
                var a = {
                    name: name.value,
                    user_id: currentUser.value.id,
                    enum_kind_of_mark_id: 2
                    
                };
                await http.put('/addHandMadeKindOfMark/' + currentUser.value.id, a);
                showAlert.value = true
                setTimeout(() => {
                    showAlert.value = false;
                }, 1000);
            } catch (error) {
                console.error(error);
            }
        } else {isFormValid.value = false;}
    }

</script>
