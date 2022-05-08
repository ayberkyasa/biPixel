<template>
  <v-container class="fill-height light-blue lighten-5" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="8">
        <v-card class="elevation-12">
          <v-window v-model="step">
            <v-window-item :value="1">
              <v-row>
                <v-col cols="12" md="8">
                  <v-card-text class="mt-5">
                    <h1
                      class="text-center display-2 light-blue--text text--darken-3"
                    >
                      Login to biPixel
                    </h1>
                    <v-form>
                      <v-text-field
                        label="Email"
                        name="Email"
                        prepend-icon="mdi-email"
                        v-model="authentication.email"
                        type="text"
                        color="light-blue darken-3"
                      />
                      <v-text-field
                        id="password"
                        label="Password"
                        name="password"
                        v-model="authentication.password"
                        prepend-icon="mdi-lock"
                        :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                        :type="showPassword ? 'text' : 'password'"
                        color="light-blue darken-3"
                        @click:append="showPassword = !showPassword"
                      ></v-text-field>
                    </v-form>
                  </v-card-text>
                  <div class="text-center mb-10">
                    <v-btn
                      @click="checkAuthentication"
                      color="light-blue darken-3"
                      dark
                      rounded
                      >LOGIN</v-btn
                    >
                  </div>
                </v-col>
                <v-col cols="12" md="4" class="light-blue darken-3">
                  <v-card-text class="white--text mt-12">
                    <h1 class="text-center display-1">Hello,</h1>
                    <h3 class="text-center">
                      Click Sign Up button to register the system
                    </h3>
                  </v-card-text>
                  <v-card-actions class="justify-center white--text">
                    <v-btn
                      class="light-blue lighten-5"
                      elevation="10"
                      outlined
                      rounded
                      text
                      @click="step++"
                      large
                    >
                      Sign Up
                    </v-btn>
                  </v-card-actions>
                </v-col>
              </v-row>
            </v-window-item>
            <v-window-item :value="2">
              <v-row>
                <v-col cols="12" md="8">
                  <h1
                    class="text-center light-blue--text text--darken-3 display-2 mt-5"
                  >
                    Sign up to biPixel
                  </h1>
                  <v-row>
                    <v-col cols="12" md="6">
                      <v-card-text class="">
                        <v-form>
                          <v-text-field
                            id="name"
                            label="Name"
                            v-model="registeration.fullName"
                            prepend-icon="mdi-account"
                            type="text"
                            color="light-blue darken-3"
                          />
                          <v-text-field
                            id="birthDate"
                            label="Birth Date"
                            v-model="registeration.birthDate"
                            prepend-icon="mdi-id-card"
                            type="text"
                            color="light-blue darken-3"
                          />
                        </v-form>
                      </v-card-text>
                    </v-col>
                    <v-divider class="mt-8 mb-8" vertical inset></v-divider>
                    <v-col cols="12" md="6">
                      <v-card-text class="">
                        <v-form>
                          <v-text-field
                            id="email"
                            label="Email"
                            v-model="registeration.email"
                            prepend-icon="mdi-email"
                            type="text"
                            color="light-blue darken-3"
                          />
                          <v-text-field
                            id="password"
                            label="Password"
                            name="password"
                            v-model="registeration.password"
                            prepend-icon="mdi-lock"
                            :append-icon="
                              showPassword ? 'mdi-eye' : 'mdi-eye-off'
                            "
                            :type="showPassword ? 'text' : 'password'"
                            color="light-blue darken-3"
                            @click:append="showPassword = !showPassword"
                          ></v-text-field>
                        </v-form>
                      </v-card-text>
                    </v-col>
                  </v-row>
                  <div class="text-center mb-10">
                    <v-btn @click="register" color="light-blue darken-3" dark
                      >SIGN UP</v-btn
                    >
                  </div>
                </v-col>
                <v-col cols="12" md="4" class="light-blue darken-3">
                  <v-card-text class="white--text mt-12">
                    <h1 class="text-center display-1">Hello,</h1>
                    <h3 class="text-center">
                      Click Login button to login the system
                    </h3>
                  </v-card-text>
                  <v-card-actions class="justify-center white--text">
                    <v-btn
                      class="light-blue lighten-5"
                      elevation="10"
                      outlined
                      rounded
                      text
                      @click="step--"
                      large
                    >
                      Login
                    </v-btn>
                  </v-card-actions>
                </v-col>
              </v-row>
            </v-window-item>
          </v-window>
        </v-card>
      </v-col>
    </v-row>
    <v-snackbar color="red darken-1" timeout="2000" v-model="snackbar">
      {{ text }}
    </v-snackbar>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig.js";
export default {
  data() {
    return {
      text: "",
      snackbar: false,
      authentication: { email: "", password: "" },
      registeration: {
        email: "",
        password: "",
        fullName: "",
        birthDate: "",
      },
      step: 1,
      showPassword: false,
    };
  },
  methods: {
    async checkAuthentication() {
      try {
        const response = await axiosInstance.post(URL.LOGIN, this.authentication);
        this.$store.commit("logIn", {
          id: response.data.id,
          userType: response.data.userType,
        });
        this.$router.push("/profile");
      } catch (exception) {
        this.text = "Email or password is incorrect. Please try again!";
        this.snackbar = true;
      }
    },
    async register() {
      try {
        await this.checkInputValidation();
        try {
          axiosInstance.post(URL.REGISTER, this.registeration);
          this.step--;
        } catch (exception) {
          this.text = exception.response.data.message;
          this.snackbar = true;
        }
      } catch (exception) {
        this.text = exception;
        this.snackbar = true;
      }
    },
    checkInputValidation() {
      if (
        this.registeration.email.trim().isEmpty() ||
        this.registeration.password.trim().isEmpty() ||
        this.registeration.fullName.trim().isEmpty() ||
        this.registeration.birthDate.trim().isEmpty()
      )
        throw "Any field cannot be blank!";
    },
  },
};
</script>
