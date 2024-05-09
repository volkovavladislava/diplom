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
      </ul>
      <a class="nav-link active" aria-current="page" href="/profile">Профиль</a>
    </div>
  </div>
</nav>
  </template>
  
  <script>
      export default {
          name: "NavBar",
          data() {
              return {
  
              };
          }
      };
  </script>
  
  <style>
    .item {
      margin-right: 5px;
    }
  </style>