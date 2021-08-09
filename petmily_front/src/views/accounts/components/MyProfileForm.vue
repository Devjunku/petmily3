<template>
  <br>
  <div class="d-flex jusify-content-start mx-auto" style="width: 800px">
    <h2>
      내 프로필
    </h2>
  </div>
  <div>
    <div class="Jbgc mx-auto" style="height: 400px; width: 800px;">
      <div class="d-inline position-relative inner30 d-flex justify-content-evenly align-items-center">
        <div>
          Profile Photo
        </div>
        <div>
          <p>NAME</p>
          <p>EMAIL</p>
          <p>PHONE</p>
        </div>
      </div>
    </div>
    <div class="Jbgc mx-auto d-flex justify-content-end" style="width: 800px;">
      <button class="btn-logo m-3">정보 수정 하기</button>
    </div>
  </div>
</template>
<script>
export default {
  name: 'myprofileform',
}
</script>
<style>
.btn-white {
  width: 200px;
  background-color: #A4B5F0;
  border-right: #A4B5F0 1px solid;
  border-left: #A4B5F0 1px solid;
  border-top: #A4B5F0 1px solid;
  border-bottom: #A4B5F0 1px solid;
  border-style: none;
  border-radius: 12px;
}

.inner30 {
  top: 30%;
}

.btn-logo {
  width: 10rem;
  height: 3rem;
  border-color: #B8B6F9;
  background-color: #B8B6F9;
}
</style>