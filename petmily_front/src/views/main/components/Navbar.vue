<template>
  <div>
    <div id="nav" class="mb-1">
      <div>
        <!-- style="box-shadow: 0 4px 0 0 violet; content-box" 여부 다시 결정 해야함. -->
        <router-link to="/" class="mx-5 text-decoration-none"
          ><img
            src="@\assets\PetmilyLogo.png"
            style="width: 50px; height: 50px; border-radius: 50%"
        /></router-link>
        <router-link to="/" class="mx-5 text-decoration-none"
          >Home</router-link
        >
        <router-link to="/animallist" class="mx-5 text-decoration-none"
          >유기동물</router-link
        >
        <router-link to="/community" class="mx-5 text-decoration-none"
          >커뮤니티</router-link
        >
        <router-link to="/live" class="ms-3 me-5 text-decoration-none"
          ><img
            src="@\assets\live.png"
            style="width: 20px; height: 40px"
          />라이브</router-link
        >

        <span v-if="confirmLogin">
          <button
            id="btn"
            type="button"
            class="btn btn-link mx-5 text-decoration-none"
            @click="logout"
            style="margin-bottom: 1px"
          >
            로그아웃
          </button>
          <span v-if="isUser">
            <router-link to="/profileprivate" class="mx-5 text-decoration-none"
              >마이페이지</router-link
            >
          </span>
          <span v-if="isAgency">
            <router-link to="/profileagency" class="mx-5 text-decoration-none"
              >마이페이지</router-link
            >
          </span>
        </span>
        <span v-else>
          <router-link to="/login" class="mx-5 text-decoration-none"
            >로그인</router-link
          >
          <router-link to="/signupterms" class="mx-5 text-decoration-none"
            >회원가입</router-link
          >
        </span>
      </div>
    </div>
    <router-view />
  </div>
</template>
<script>
import { computed } from "vue";

export default {
  name: "Navbar",
  setup() {
    const confirmLogin = computed(() => {
      return sessionStorage.getItem("isLogin");
    });

    const logout = function () {
      localStorage.clear()
      sessionStorage.clear()
      location.href="/"
    };

    const isAgency = computed(() => {
      return sessionStorage.getItem("isAgency");
    });

    const isUser = computed(() => {
      return sessionStorage.getItem("isUser");
    });

    return { confirmLogin, logout, isAgency, isUser };
  },
};
</script>

<style>
.footer {
  color: aliceblue;
  margin: 100px;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
  box-shadow: 0 2px 3px 0 #edddf2;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#btn {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #9aaef6;
}
</style>
