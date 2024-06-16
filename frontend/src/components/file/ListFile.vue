<template>
    <div class="container labelm">
        <div class="row justify-content-md-center labelm" v-if="displayContent">

    
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
                           {{ moment.utc(i.date).tz('Asia/Singapore').format('YYYY-MM-DD HH:mm')}}
                            </div>
                            <div class="col ">
                            {{i.name}}
                            </div>
                        </div>
                        </div>
                    </div>
            </div>
        </div>
        <div v-else>
            {{ content.message }}
        </div>
    </div>
</template>

<script setup>
  import { ref, onMounted,computed } from 'vue';
  import http from "../../http-common";
  import { useRouter } from 'vue-router';
  import { useStore } from 'vuex';
  import UserService from '../../services/user.service';
  import moment from 'moment-timezone';

	const store = useStore();
	const currentUser = computed(() => store.state.auth.user);

  // const moment = require('moment');
  const router = useRouter();

//   const userId = ref(1)
  const files = ref([])

    const displayContent= ref(false)
    const content = ref('')

    const redirectToUpdateFilePage = (id, data) => {
        const encodedData = encodeURIComponent(JSON.stringify(data));
        router.push({ path: '/updateFile/' + id, query: { data: encodedData } });
    };

    const redirectToAddFilePage = () => {
        router.push({ path: '/addFile' });
    };


  const getListFile = async () => {
      try {
          const response = await http.get('/fileByUser/' + currentUser.value.id);
          files.value = []
          response.data.forEach(value => {
                files.value.push(value)         
              
          });

      } catch (error) {
          // console.error(error);
            if(error.response.status == 401 || error.response.status == 403){
            await store.dispatch('auth/logout')
            router.push('/login')
            }
      }
  }

  const fetchUserBoard = async () => {
    try {
      await UserService.getUserBoard()
      displayContent.value = true
    } catch (e) {
      // displayContent.value = false
      content.value = (e.response && e.response.data) || e.message || e.toString()
    }
  }

  onMounted(async () => {
    fetchUserBoard()
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