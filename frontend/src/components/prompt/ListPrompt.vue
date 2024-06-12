<template>
    <div class="container labelm" >
        <div class="row justify-content-md-center labelm" v-if="displayContent">

    
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
        <div v-else>
            {{ content.message }}
        </div>
    </div>
</template>

<script setup>
  import { ref, onMounted, computed } from 'vue';
  import http from "../../http-common";
  import { useRouter } from 'vue-router';
  import { useStore } from 'vuex';
    import UserService from '../../services/user.service';
  
  const store = useStore();
  const currentUser = computed(() => store.state.auth.user);
  
  const moment = require('moment');
  const router = useRouter();

   const displayContent= ref(false)
  const content = ref('')

//   const userId = ref(1)
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
          const response = await http.get('/promptByUser/' + currentUser.value.id);
          prompts.value = []
          response.data.forEach(value => {
                prompts.value.push(value)         
              
          });

          console.log(prompts.value)
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