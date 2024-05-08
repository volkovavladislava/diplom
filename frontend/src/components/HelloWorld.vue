<template>
  <div v-if="user"  class="hello">
    <h1>{{ msg }}</h1>
    <p>
      For a guide and recipes on how to configure / customize this project,<br>
      check out the
      <a href="https://cli.vuejs.org" target="_blank" rel="noopener">vue-cli documentation</a>.
    </p>
    <h3>Installed {{user[0].name}}</h3>

  </div>
</template>

<script>
import http from "../http-common";
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data() {
      return {
          user: null,
      };
  },
  methods: {
    async getCourse() {
        //console.log(1)
        await http
            .get("/listUser") // обращаемся к серверу для получения списка пользователей, id взят из входных параметров (props)
            .then(response => { // запрос выполнен успешно
                this.user = response.data;
            })
            .catch(e => { // запрос выполнен с ошибкой
                console.log(e);
            });
            
    }
  },
  async mounted() { 
      await this.getCourse();  
  }
}
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
