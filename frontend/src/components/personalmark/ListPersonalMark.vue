<template>
    <div class="container labelm" >

      <div class="row justify-content-md-center labelm" v-if="displayContent">

        <div  class="col-md-7">
            <button type="button" class="btn btn-outline-secondary bthM"  @click="redirectToAddPersonalMarkPage()">Добавить собственный показатель</button>
        </div>
        <div  class="col-md-7">

          <div class="accordion " id="accordionFlushExample">

            <div class="accordion-item"  v-for="(kindOfMark, index) in kindOfMarks" :key="index">
              <h2 class="accordion-header" :id="'headingOne-' + index">
                <button class="accordion-button collapsed" type="button" :data-bs-toggle="'collapse'" :data-bs-target="'#collapseOne-' + index" aria-expanded="false" aria-controls="collapseOne">
                   <div  class="col-md-3">
                      {{kindOfMark.name}}
                   </div>
                  <div class="favorite">
                    <svg width="20px" height="20px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" v-if="kindOfMark.has_reference == 0" @click="addFavorite(kindOfMark.id)">
                      <path d="M4.45067 13.9082L11.4033 20.4395C11.6428 20.6644 11.7625 20.7769 11.9037 20.8046C11.9673 20.8171 12.0327 20.8171 12.0963 20.8046C12.2375 20.7769 12.3572 20.6644 12.5967 20.4395L19.5493 13.9082C21.5055 12.0706 21.743 9.0466 20.0978 6.92607L19.7885 6.52734C17.8203 3.99058 13.8696 4.41601 12.4867 7.31365C12.2913 7.72296 11.7087 7.72296 11.5133 7.31365C10.1304 4.41601 6.17972 3.99058 4.21154 6.52735L3.90219 6.92607C2.25695 9.0466 2.4945 12.0706 4.45067 13.9082Z" stroke="#222222"/>
                    </svg>

                    <svg width="20px" height="20px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" v-else  @click="deleteFavorite(kindOfMark.id)">
                      <g id="SVGRepo_bgCarrier" stroke-width="0"/>
                      <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"/>
                      <g id="SVGRepo_iconCarrier"> <path d="M4.45067 13.9082L11.4033 20.4395C11.6428 20.6644 11.7625 20.7769 11.9037 20.8046C11.9673 20.8171 12.0327 20.8171 12.0963 20.8046C12.2375 20.7769 12.3572 20.6644 12.5967 20.4395L19.5493 13.9082C21.5055 12.0706 21.743 9.0466 20.0978 6.92607L19.7885 6.52734C17.8203 3.99058 13.8696 4.41601 12.4867 7.31365C12.2913 7.72296 11.7087 7.72296 11.5133 7.31365C10.1304 4.41601 6.17972 3.99058 4.21154 6.52735L3.90219 6.92607C2.25695 9.0466 2.4945 12.0706 4.45067 13.9082Z" fill="#f18484" stroke="#f18484" stroke-width="2"/> </g>
                    </svg>
                  </div>
                  
                </button>
              </h2>
              <div :id="'collapseOne-' + index" class="accordion-collapse collapse " aria-labelledby="headingOne" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                  <div class="">
                    Выберите добавить данные по показателю или посмотреть статистику.
                  </div>
                  <div class="">
                    <button type="button" class="btn  btn-outline-secondary bthM" @click="redirectToAddRecordPersonalMarkPage(kindOfMark.id, kindOfMark)">Добавить данные</button>
                    <button type="button" class="btn btn-outline-secondary bthM"  @click="redirectToDetailedPersonalMarkPage(kindOfMark.id, kindOfMark)" >Просмотр статистики</button>
                    <button type="button" class="btn  btn-outline-danger " @click="deletePersonalMark(kindOfMark.id)">Удалить показатель</button>
                  </div>

                  
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

	const store = useStore();
	const currentUser = computed(() => store.state.auth.user);

  const kindOfMarks = ref([])
  const router = useRouter();
  // const userId = ref(1)

  const displayContent= ref(false)
  const content = ref('')

  const redirectToAddPersonalMarkPage = () => {
      router.push({ path: '/addPersonalMark'});
  };



  const redirectToAddRecordPersonalMarkPage = (id, data) => {
    const encodedData = encodeURIComponent(JSON.stringify(data));
    router.push({ path: '/addRecordPersonalMark/' + id, query: { data: encodedData } });
  };

  const redirectToDetailedPersonalMarkPage = (id, data) => {
    const encodedData = encodeURIComponent(JSON.stringify(data));
    router.push({ path: '/detailedPersonalMark/' + id, query: { data: encodedData } });
  };

  const getListKindOfMark = async () => {
      try {
          const response = await http.get('/listKindOfMarkOfHandMade/' + currentUser.value.id);
          kindOfMarks. value = []
          response.data.forEach(value => {
                kindOfMarks.value.push(value)         
              
          });
      } catch (error) {
          // console.error(error);
          if(error.response.status == 401 || error.response.status == 403){
          await store.dispatch('auth/logout')
          router.push('/login')
          }
      }
  }

  async function addFavorite(id){
    try {
        var data = {
            user_id: currentUser.value.id,
            kind_of_mark_id: id
        };
        await http.put('/addFavoriteKindOfMark', data);
         getListKindOfMark();
        } catch (error) {
            // console.error(error);
            if(error.response.status == 401 || error.response.status == 403){
            await store.dispatch('auth/logout')
            router.push('/login')
            }
        }
  }

  async function deleteFavorite(id){
     try {
        var data = {
            user_id: currentUser.value.id,
            kind_of_mark_id: id
        };
        await http.post('/deleteFavoriteKindOfMark', data);
         getListKindOfMark();
        } catch (error) {
            // console.error(error);
            if(error.response.status == 401 || error.response.status == 403){
            await store.dispatch('auth/logout')
            router.push('/login')
            }
        }
  }

  async function deletePersonalMark(id){
     try {
        var data = {
            user_id: currentUser.value.id,
            kind_of_mark_id: id
        };
        
        await http.post('/deletePersonalMark', data);
         getListKindOfMark();
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
    await getListKindOfMark();
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
  