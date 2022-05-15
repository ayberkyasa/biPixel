<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Social Network</v-row>
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name or email if you know"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-tabs color="deep-blue accent-4" v-model="tab">
      <v-tabs-slider color="blue"></v-tabs-slider>
      <v-tab>Add Friend</v-tab>
      <v-tab>Remove Friend {{ friendCount }}</v-tab>
      <v-tab>Sent and Pending Requests {{ sentCount }}</v-tab>
      <v-tab>Received and Pending Requests {{ receiveCount }}</v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item>
        <v-data-table
          :headers="headers"
          :items="nonFriends"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
          <template v-slot:[`item.request`]="{ item }">
            <v-btn
              color="green darken-1"
              rounded
              small
              dark
              @click="addFriend(item)"
              ><v-icon dark>mdi-account-plus</v-icon></v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers"
          :items="friends"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
          <template v-slot:[`item.request`]="{ item }">
            <v-btn
              color="red darken-1"
              rounded
              small
              dark
              @click="removeFriend(item)"
              ><v-icon dark>mdi-account-remove</v-icon></v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers2"
          :items="sentPending"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers3"
          :items="receivedPending"
          item-key="email"
          class="elevation-1"
          :search="search"
        >
          <template v-slot:[`item.accept`]="{ item }">
            <v-btn
              color="green darken-1"
              rounded
              small
              dark
              @click="acceptRequest(item)"
              ><v-icon dark>mdi-checkbox-marked-circle</v-icon></v-btn
            >
          </template>
          <template v-slot:[`item.reject`]="{ item }">
            <v-btn
              color="red darken-1"
              rounded
              small
              dark
              @click="rejectRequest(item)"
              ><v-icon dark>mdi-close-circle</v-icon></v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
    </v-tabs-items>
    <v-row class="display-1 font-weight-medium mt-6 mb-6"
      >Recommend Movie</v-row
    >
    <v-row
      ><v-text-field
        v-model="searchForRec"
        label="Search by name or email if you know"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-data-table
      :headers="headers4"
      :items="movies"
      item-key="email"
      class="elevation-1"
      :search="searchForRec"
    >
      <template v-slot:[`item.recommend`]="{ item }">
        <v-btn
          color="orange lighten-1"
          small
          dark
          @click="showedMovie = item"
          @click.stop="openRecDialog(item)"
          >Recommend</v-btn
        >
      </template>
    </v-data-table>
    <v-dialog v-model="recDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5">Recommend</v-card-title>
        <v-card-text class="pl-9">
          <v-row align="center">
            <v-col cols="12">
              <v-select
                :items="friends"
                v-model="selectedFriend"
                :menu-props="{ top: true, offsetY: true }"
                label="Select a Friend"
                item-text="full_name"
                return-object
              ></v-select>
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn dark color="green darken-1" @click="recommend">
            RECOMMEND
          </v-btn>
          <v-btn dark color="red darken-1" @click="recDialog = false">
            CLOSE
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-snackbar :color="color" timeout="2000" v-model="snackbar">
      {{ mes }}
    </v-snackbar>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      friendCount: "",
      sentCount: "",
      receiveCount: "",
      mes: "",
      snackbar: false,
      color: "",
      recDialog: false,
      selectedFriend: {},
      searchForRec: "",
      search: "",
      showedMovie: {},
      movies: [],
      nonFriends: [],
      friends: [],
      sentPending: [],
      receivedPending: [],
      tab: null,
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
          align: " d-none",
          value: "email",
          sortable: false,
        },
        {
          text: "Send Request",
          sortable: false,
          align: "center",
          value: "request",
        },
      ];
    },
    headers2() {
      return [
        {
          text: "Full Name",
          align: "start",
          value: "full_name",
          sortable: false,
        },
        {
          text: "Email",
          align: " d-none",
          value: "email",
          sortable: false,
        },
      ];
    },
    headers3() {
      return [
        {
          text: "Full Name",
          align: "start",
          value: "full_name",
          sortable: false,
        },
        {
          text: "Email",
          align: " d-none",
          value: "email",
          sortable: false,
        },
        {
          text: "Accept",
          sortable: false,
          align: "center",
          value: "accept",
        },
        {
          text: "Reject",
          sortable: false,
          align: "center",
          value: "reject",
        },
      ];
    },
    headers4() {
      return [
        {
          text: "Title",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Director",
          align: "start",
          value: "directors_full_name",
          filterable: false,
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genres",
        },
        {
          text: "Rate",
          align: "start",
          value: "overall_rating",
          sortable: false,
        },
        {
          text: "Year",
          align: "start",
          filterable: false,
          value: "production_year",
          sortable: false,
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
          sortable: false,
        },
        {
          text: "Recommend",
          align: "center",
          value: "recommend",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    async recommend() {
      if (Object.keys(this.selectedFriend).length != 0) {
        this.recDialog = false;
        try {
          const res = await axiosInstance.post(URL.RECOMMEND_MOVIE, {
            recommender: parseInt(this.$store.state.uid),
            friend: parseInt(this.selectedFriend.user_id),
            movieId: parseInt(this.showedMovie.movie_id),
          });
          this.mes = res.data.result;
          this.color = "green lighten-1";
          this.snackbar = true;
        } catch (error) {
          this.mes = error.response.data.result;
          this.color = "red lighten-1";
          this.snackbar = true;
        }
        this.selectedFriend = {};
      }
    },
    openRecDialog() {
      this.recDialog = true;
    },
    async initialize() {
      try {
        const nonFriends = await axiosInstance.get(URL.GET_NON_FRIENDS, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        this.nonFriends = nonFriends.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const friends = await axiosInstance.get(URL.GET_FRIENDS, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        if (friends.data.length !== 0) {
          this.friendCount = `(${friends.data.length})`;
        }
        this.friends = friends.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const receiving = await axiosInstance.get(
          URL.GET_RECEIVING_FRIEND_REQUESTS,
          {
            params: {
              userId: this.$store.state.uid,
            },
          }
        );
        if (receiving.data.length !== 0) {
          this.receiveCount = `(${receiving.data.length})`;
        }
        this.receivedPending = receiving.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const sent = await axiosInstance.get(URL.GET_SENT_FRIEND_REQUESTS, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        if (sent.data.length !== 0) {
          this.sentCount = `(${sent.data.length})`;
        }
        this.sentPending = sent.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const movies = await axiosInstance.get(URL.GET_ALL_MOVIES);
        this.movies = movies.data;
      } catch (error) {
        console.log(error);
      }
    },
    async addFriend(item) {
      try {
        const res = await axiosInstance.post(URL.ADD_FRIEND, {
          whoSendRequest: this.$store.state.uid,
          whoReceiveRequest: item.user_id,
        });
        await this.initialize();
        console.log(res);
      } catch (error) {
        console.log(error);
      }
    },
    async removeFriend(item) {
      try {
        const res = await axiosInstance.delete(URL.DELETE_FRIEND, {
          data: {
            whoDelete: this.$store.state.uid,
            whoIsDeleted: item.user_id,
          },
        });
        console.log(res);
        await this.initialize();
      } catch (error) {
        console.log(error);
      }
      return item;
    },
    async acceptRequest(item) {
      try {
        const res = await axiosInstance.post(URL.ANSWER_REQUESTS, {
          requestId: "true",
          whoSendRequest: item.user_id,
          whoAnswerRequest: this.$store.state.uid,
        });
        console.log(res);
        await this.initialize();
      } catch (error) {
        console.log(error);
      }
    },
    async rejectRequest(item) {
      try {
        const res = await axiosInstance.post(URL.ANSWER_REQUESTS, {
          requestId: "false",
          whoSendRequest: item.user_id,
          whoAnswerRequest: this.$store.state.uid,
        });
        console.log(res);
        await this.initialize();
      } catch (error) {
        console.log(error);
      }
    },
  },
  async created() {
    await this.initialize();
  },
};
</script>
