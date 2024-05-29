<template>
<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #FFD393;">
  <div class="container-fluid">
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link" aria-current="page" href="/listSystemMarks">Список показателей</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/listPrompt">Мои записи</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/listFile">Мои файлы</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/listPersonalMarks">Мои персональные показатели</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="/guestMode">Гостевой доступ</a>
        </li>
      </ul>
      <!-- <a class="nav-link active" aria-current="page" href="/profile">Профиль</a> -->
      <div v-if="currentUser">
        <a class="nav-link active" aria-current="page" href="/profile">Профиль</a>

        <a href @click.prevent="logOut" class="navbar-brand text-light">
            Выйти
        </a>
        </div>
    <div v-else>
        <router-link to="/login" class="navbar-brand text-light">
            Войти
        </router-link>
    </div>
    </div>
  </div>
</nav>
</template>
  
<script>
  export default {
    name: "NavBar",
    data() {
        return {};

    },
    computed: { // вычисляемые свойства
        currentUser() {
            return this.$store.state.auth.user;
        }
    },
    methods: {
        logOut() {
            this.$store.dispatch('auth/logout'); // обращаемся к методу logout, который определён в auth.service.js
            window.location.href = '/login'; // // Используем такую конструкцию, а не this.$router.push, так как требуется перезагрузить страницу для обновления локального хранилища
        }
    }
};
</script>
  
<style>
  .item {
    margin-right: 5px;
  }
</style>