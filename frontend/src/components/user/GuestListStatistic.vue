<template>
    <div class="container labelm">
        <button type="button" class="btn btn-outline-danger top-left-button" @click="logOutGuest()">Выйти из гостевого доступа</button>

        <div class="row justify-content-md-center labelm">

    
            <div class="row justify-content-md-center ">
                <h5 class="text-center fs-3 col-md-5"> Выберите тип показателя</h5>
            </div>

            <div >
                <button type="button" class="btn btn-outline-secondary bthM" @click="getListKindOfMark()">Базовые показатели</button>
                <!-- <br/> -->
                <button type="button" class="btn btn-outline-secondary bthM" @click="getListKindOfMarkPersonal()">Персональные показатели</button>
            </div>


            <div  class="col-md-5" v-if="kindOfMarks.length">
                <div class="card"    v-for="i  in kindOfMarks" :key="i.id"  @click="redirectToGuestStatisticPage(i.id, i, data)" >
                        <div class="card-content">
                        <div class="row align-items-center">
                            <div class="col ">
                                {{i.name}}
                            </div>
                        </div>
                        </div>
                    </div>
            </div>

            <div  class="col-md-5" v-if="kindOfMarksPersonal.length">
                <div class="card"    v-for="i  in kindOfMarksPersonal" :key="i.id"  @click="redirectToGuestPersonalStatisticPage(i.id, i, data)" >
                        <div class="card-content">
                        <div class="row align-items-center">
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
import { useRouter } from 'vue-router'
import { ref, onMounted,computed  } from 'vue';
import http from "../../http-common";
import { usePiniastore } from '../../store/piniastore'
	
    const piniastore = usePiniastore()

    const router = useRouter();
    // const data = ref(JSON.parse(decodeURIComponent(router.currentRoute.value.query.data, null, 2)))
    const data = computed(() => piniastore.getGuestdata)

    const kindOfMarks = ref([])
    const kindOfMarksPersonal = ref([])


  const redirectToGuestPersonalStatisticPage = (id, data, data2) => {
    // const encodedData = encodeURIComponent(JSON.stringify(data));
    // const encodedData2 = encodeURIComponent(JSON.stringify(data2));
    
    piniastore.setKindOfMarkPersonalData(data)
    piniastore.setGuestdata(data2)
    router.push({ path: '/guestPersonalStatistic' });
    // router.push({ path: '/guestPersonalStatistic' , query: { data1: encodedData, data2: encodedData2 } });
  };

  const redirectToGuestStatisticPage = (id, data, data2) => {
    // const encodedData = encodeURIComponent(JSON.stringify(data));
    // const encodedData2 = encodeURIComponent(JSON.stringify(data2));

    piniastore.setKindOfMarkData(data)
    piniastore.setGuestdata(data2)
    router.push({ path: '/guestStatistic' });
    // router.push({ path: '/guestStatistic' , query: { data1: encodedData, data2: encodedData2 } });
  };
    

    const getListKindOfMark = async () => {
      try {
          const response = await http.get('/listKindOfMarkOfSystem');
          kindOfMarksPersonal.value = []
          kindOfMarks.value = []
          response.data.forEach(value => {
              if(value.id == 1){
                kindOfMarks.value.push({
                  id: 1,
                  name: "Давление",
                  user_id: value.user_id,
                  enum_kind_of_mark_id: value.enum_kind_of_mark_id,
                  has_reference: value.has_reference
                })
              }
              if(value.id != 2 && value.id != 1){
                  kindOfMarks.value.push(value)
              }              
              
          });
      } catch (error) {
          console.error(error);
      }
  }

   const getListKindOfMarkPersonal = async () => {
      try {
          const response = await http.get('/listKindOfMarkOfHandMade/' + data.value.id);
          kindOfMarks.value = []
          kindOfMarksPersonal.value = []
          response.data.forEach(value => {
                kindOfMarksPersonal.value.push(value)         
          });
      } catch (error) {
          console.error(error);
      }
  }

  const logOutGuest = async () => {
      try {
            var a = {
                id: data.value.id 
            };
            await http.post('/logoutGuest', a);
            router.push({ path: '/guestMode' });
          
      } catch (error) {
          console.error(error);
      }
  }


    const checkGuest = async () => {
        try {
            var a = {
                login: data.value.login,
            };
             await http.post('/checkGuest' , a);
        } catch (error) {
            if(error.response.status == 401 || error.response.status == 404 || error.response.status == 403){
                router.push({ path: '/guestMode' });
            }
        }
    }

onMounted(async () => {
    await checkGuest();
});


</script>

<style scoped>

.top-left-button {
    position: absolute;
    top: 650px;
    left: 1250px;
}

.card {
  border-radius: 8px;
  padding: 10px;
  background-color: #ffffff;
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.card-content {
  display: flex;
  justify-content:center;
}


</style>