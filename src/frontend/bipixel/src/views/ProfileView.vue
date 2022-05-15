<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Profile</v-row>
    <v-row>
      <v-col cols="6">
        <v-text-field
          :value="information.full_name"
          label="Name"
          outlined
          disabled
          dense
          rounded
          class="mb-6"
        ></v-text-field>
      </v-col>
      <v-col cols="6">
        <v-text-field
          :value="information.birth_date"
          label="Birth Date"
          outlined
          disabled
          dense
          rounded
          class="mb-6"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6">
        <v-text-field
          :value="information.email"
          label="E-mail"
          outlined
          disabled
          dense
          rounded
          class="mb-6"
        ></v-text-field>
      </v-col>
      <v-col cols="6">
        <v-text-field
          :value="information.status"
          label="Status"
          outlined
          disabled
          dense
          rounded
          class="mb-6"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row class="display-1 font-weight-medium mb-6">Information</v-row>
    <v-tabs color="deep-blue accent-4" v-model="tab">
      <v-tabs-slider color="blue"></v-tabs-slider>
      <v-tab>Past Rents</v-tab>
      <v-tab>Current Rents</v-tab>
      <v-tab>Recommendations From Friends</v-tab>
      <v-tab>Rating and Review</v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item>
        <v-data-table
          :headers="headers"
          :items="pastRents"
          item-key="email"
          class="elevation-1"
        >
          <template v-slot:[`item.review`]="{ item }">
            <v-btn
              color="green lighten-1"
              rounded
              small
              dark
              @click="review(item)"
              @click.stop="reviewDialog = true"
              >Review</v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers2"
          :items="currentRents"
          item-key="email"
          class="elevation-1"
        >
          <template v-slot:[`item.review`]="{ item }">
            <v-btn
              color="green lighten-1"
              rounded
              small
              dark
              @click="review(item)"
              @click.stop="reviewDialog = true"
              >Review</v-btn
            >
          </template>
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers1"
          :items="recommendationsFromFriends"
          item-key="email"
          class="elevation-1"
        >
        </v-data-table>
      </v-tab-item>
    </v-tabs-items>
    <v-dialog v-model="reviewDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3">
          {{ showedMovie.title }}
        </v-card-title>

        <v-card-text class="pl-9">
          <v-textarea
            counter
            background-color="grey lighten-3"
            label="Comment"
            :rules="rules"
            v-model="comment"
          ></v-textarea>
          <v-select
            :items="rates"
            v-model="rate"
            background-color="grey lighten-3"
            :menu-props="{ top: true, offsetY: true }"
            label="Rate"
          ></v-select>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="submit"> Submit </v-btn>
          <v-btn color="red darken-1" text @click="reviewDialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axiosInstance, { URL } from "@/services/axiosConfig";
export default {
  data() {
    return {
      rate: 10,
      comment: "",
      rates: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      rules: [(v) => v.length <= 300 || "Max 300 characters"],
      pastRents: [],
      reviewDialog: false,
      currentRents: [],
      recommendationsFromFriends: [],
      showedMovie: {},
      color: "",
      text: "",
      snackbar: false,
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
      information: {
        full_name: "",
        birth_date: "",
        email: "",
        status: "",
      },
      tab: null,
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "Title",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
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
          text: "Rent Date",
          align: "start",
          value: "rent_date",
          filterable: false,
          sortable: false,
        },
        {
          text: "Last Renew Date",
          sortable: false,
          align: "start",
          value: "last_renew_date",
        },
        {
          text: "Renew Times",
          align: "start",
          value: "renew_times",
          sortable: false,
        },
        {
          text: "Review",
          align: "center",
          value: "review",
          sortable: false,
        },
      ];
    },
    headers1() {
      return [
        {
          text: "Title",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Duration",
          sortable: false,
          align: "start",
          value: "duration",
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
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
          text: "Genres",
          align: "start",
          value: "genres",
          filterable: false,
          sortable: false,
        },
        {
          text: "Recommender",
          align: "start",
          value: "recommender",
          sortable: false,
        },
      ];
    },
    headers2() {
      return [
        {
          text: "Title",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
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
          text: "Rent Date",
          align: "start",
          value: "rent_date",
          filterable: false,
          sortable: false,
        },
        {
          text: "Last Renew Date",
          sortable: false,
          align: "start",
          value: "last_renew_date",
        },
        {
          text: "Due Date",
          sortable: false,
          align: "start",
          value: "due_date",
        },
        {
          text: "Renew Times",
          align: "start",
          value: "renew_times",
          sortable: false,
        },
      ];
    },
  },
  methods: {
    review(item) {
      console.log(item);
      this.showedMovie = item;
    },
    async submit() {
      if ((this.comment !== "") & (this.comment.length <= 300)) {
        console.log(this.rate);
        this.reviewDialog = false;

        console.log(this.showedMovie);
        try {
          const res = await axiosInstance.post(URL.SEND_REVIEW, {
            movieId: this.showedMovie.movie_id,
            userId: this.$store.state.uid,
            reviewComment: this.comment,
          });

          const res2 = await axiosInstance.post(URL.SEND_RATE, {
            movieId: this.showedMovie.movie_id,
            userId: this.$store.state.uid,
            ratingScore: this.rate,
          });
          console.log(res);
          console.log(res2);
        } catch (error) {
          console.log(error);
        }
        this.comment = "";
        this.rate = 10;
      }
    },
    async initialize() {
      try {
        const past = await axiosInstance.get(URL.GET_MOVIE_HISTORY, {
          params: {
            userId: parseInt(this.$store.state.uid),
          },
        });
        this.pastRents = past.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const current = await axiosInstance.get(URL.GET_ALL_RENTED_MOVIES, {
          params: {
            key: "",
            userId: this.$store.state.uid,
          },
        });
        this.currentRents = current.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const recommendations = await axiosInstance.get(
          URL.GET_RECOMMENDED_MOVIES,
          {
            params: {
              userId: this.$store.state.uid,
            },
          }
        );
        this.recommendationsFromFriends = recommendations.data;
      } catch (error) {
        console.log(error);
      }

      try {
        const user = await axiosInstance.get(URL.GET_USER, {
          params: {
            id: this.$store.state.uid,
          },
        });
        this.information.full_name = user.data.full_name;
        this.information.email = user.data.email;
        this.information.birth_date = user.data.birth_date;
        this.information.status = this.$store.state.userType;
      } catch (error) {
        console.log(error);
      }
    },
  },
  created() {
    this.initialize();
  },
};
</script>
