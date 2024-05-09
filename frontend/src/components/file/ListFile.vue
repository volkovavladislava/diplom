<template>
    <div class="container labelm">
        <div class="row justify-content-md-center labelm">

    
            <div class="row justify-content-md-center ">
                <h5 class="text-center fs-3 col-md-5"> Список файлов</h5>
            </div>

            <div  class="col-md-7">
                <button type="button" class="btn btn-outline-secondary bthM"  @click="redirectToAddFilePage()">Добавить файл</button>
            </div>

            <div  class="col-md-7" v-if="files.length">
                <div class="card"    v-for="i  in files" :key="i.id"  @click="redirectToUpdateFilePage(i.id, i)" >
                        <div class="card-content">
                        <div class="row align-items-center">
                            <div class="col">
                           {{ moment.utc(i.date).format('YYYY-MM-DD HH:mm')}}
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
  const files = ref([])

    const redirectToUpdateFilePage = (id, data) => {
        const encodedData = encodeURIComponent(JSON.stringify(data));
        router.push({ path: '/updateFile/' + id, query: { data: encodedData } });
    };

    const redirectToAddFilePage = () => {
        router.push({ path: '/addFile' });
    };


  const getListFile = async () => {
      try {
          const response = await http.get('/fileByUser/' + userId.value);
          files.value = []
          response.data.forEach(value => {
                files.value.push(value)         
              
          });

      } catch (error) {
          console.error(error);
      }
  }

  onMounted(async () => {
    await getListFile();
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