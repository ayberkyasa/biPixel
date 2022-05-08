<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6"
      >Customer Control Panel</v-row
    >
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name or email"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-data-table
      :headers="headers"
      :items="customers"
      item-key="email"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.delete`]="{ item }">
        <v-btn
          color="red darken-1"
          rounded
          small
          dark
          @click="deleteCustomer(item)"
          ><v-icon dark>mdi-delete-forever</v-icon></v-btn
        >
      </template>
    </v-data-table>
    <v-snackbar :color="color" timeout="2000" v-model="snackbar">
      {{ text }}
    </v-snackbar>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      color: "",
      text: "",
      snackbar: false,
      search: "",
      customers: [],
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "Full Name",
          align: "start",
          value: "full_name",
          sortable: false,
        },
        {
          text: "Email",
          align: "start",
          value: "email",
          sortable: false,
        },
        {
          text: "Birth Date",
          sortable: false,
          align: "start",
          filterable: false,
          value: "birth_date",
        },
        {
          text: "Delete",
          sortable: false,
          align: "center",
          value: "delete",
        },
      ];
    },
  },
  methods: {
    async deleteCustomer(item) {
      try {
        const response = await axiosInstance.delete(URL.DELETE_CUSTOMER, {
          data: { userId: item.user_id },
        });
        this.customers = await this.getAllCustomer();
        this.text = response.data.message;
        this.color = "green lighten-1";
        this.snackbar = true;
      } catch (exception) {
        this.text = exception.response.data.message;
        this.color = "red darken-1";
        this.snackbar = true;
      }
    },
    async getAllCustomer() {
      try {
        const response = await axiosInstance.get(URL.GET_ALL_CUSTOMERS);
        return response.data;
      } catch (exception) {
        return [];
      }
    },
  },
  async created() {
    this.customers = await this.getAllCustomer();
  },
};
</script>
