<template>
    <div class="container labelm">
        <div class="row justify-content-md-center labelm">

    
            <div class="row justify-content-md-center ">
                <h5 class="text-center fs-3 col-md-5"> Список записей</h5>
            </div>

            <div  class="col-md-7">
                <button type="button" class="btn btn-outline-secondary bthM"  @click="redirectToAddPromptPage()">Добавить запись</button>
            </div>

            <div  class="col-md-7" v-if="prompts.length">
                <div class="card"    v-for="i  in prompts" :key="i.id"  @click="redirectToUpdatePromptPage(i.id, i)" >
                        <div class="card-content">
                        <div class="row align-items-center">
                            <div class="col">
                           {{ moment(i.date).format('YYYY-MM-DD HH:mm')}}
                            </div>
                            <div class="col ">
                            {{i.name}}
                            </div>
                        </div>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import http from "../../http-common";
  import { useRouter } from 'vue-router';

  const moment = require('moment');
  const router = useRouter();

  const userId = ref(1)
  const prompts = ref([])

    const redirectToUpdatePromptPage = (id, data) => {
        const encodedData = encodeURIComponent(JSON.stringify(data));
        router.push({ path: '/updatePrompt/' + id, query: { data: encodedData } });
    };

    const redirectToAddPromptPage = () => {
        router.push({ path: '/addPrompt' });
    };


  const getListPrompt = async () => {
      try {
          const response = await http.get('/promptByUser/' + userId.value);
          prompts.value = []
          response.data.forEach(value => {
                prompts.value.push(value)         
              
          });

          console.log(prompts.value)
      } catch (error) {
          console.error(error);
      }
  }

  onMounted(async () => {
    await getListPrompt();
});
</script>


<style>

.labelm {
    margin-top: 20px;
}

.bthM{
    margin: 20px;
}

.favorite{
  margin-left: 470px;
}
</style>