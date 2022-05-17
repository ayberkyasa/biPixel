<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Withdraw a Movie</v-row>
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-row>
      <v-col cols="2"> Select Upper Threshold of Price: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="price"
          label="Enter a price"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
      <v-col cols="2"> Select Lower Threshold of Rate: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="rate"
          label="Enter a rate between 1 and 10"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
    </v-row>
    <v-data-table
      :headers="headers"
      :items="rentedMovies"
      item-key="email"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          icon
          large
          @click="openDialog(item)"
          @click.stop="detailsDialog = true"
          ><v-icon>mdi-information</v-icon></v-btn
        >
      </template>
      <template v-slot:[`item.addFav`]="{ item }">
        <v-btn icon color="pink" v-if="checkFav(item)">
          <v-icon>mdi-heart</v-icon>
        </v-btn>
        <v-btn
          icon
          color="blue-grey lighten-3"
          v-if="!checkFav(item)"
          @click="addFavorites(item)"
        >
          <v-icon>mdi-heart</v-icon>
        </v-btn>
      </template>
      <template v-slot:[`item.withdraw`]="{ item }">
        <v-btn
          color="purple lighten-1"
          small
          dark
          @click="showedMovie = item"
          @click.stop="withdraw(item)"
          >Withdraw</v-btn
        >
      </template>
      <template v-slot:[`item.renew`]="{ item }">
        <v-btn
          color="blue lighten-1"
          small
          dark
          @click="showedMovie = item"
          @click.stop="renew"
          >Renew</v-btn
        >
      </template>
    </v-data-table>
    <v-divider></v-divider>
    <v-dialog v-model="detailsDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Movie Details </v-card-title>

        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Duration: {{ showedMovie.duration }} (min)</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Language Option: {{ showedMovie.language_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Subtitle Option: {{ showedMovie.subtitle_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Directors: {{ directors }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Actors: {{ actors }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Genres: {{ genres }}</strong>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="detailsDialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="payDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Payment</v-card-title>
        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Card Holder's Name</strong>
          </v-row>
          <v-row>
            <v-text-field
              v-model="holderName"
              label="Name"
              filled
              rounded
              dense
            >
            </v-text-field>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Card Number</strong>
          </v-row>
          <v-row>
            <v-text-field
              v-model="cardNumber"
              label="Card Number"
              filled
              rounded
              dense
            >
            </v-text-field>
          </v-row>
          <v-row class="text-subtitle-1">
            <v-col class="mr-3" cols="7">
              <v-row>
                <strong>Expiration Date</strong>
              </v-row>
              <v-row>
                <v-text-field
                  v-model="exDate"
                  label="Expiration Date (MM/YY)"
                  filled
                  rounded
                  dense
                >
                </v-text-field>
              </v-row>
            </v-col>
            <v-col cols="4">
              <v-row>
                <strong>CVV</strong>
              </v-row>
              <v-row>
                <v-text-field v-model="cvv" label="CVV" filled rounded dense>
                </v-text-field>
              </v-row>
            </v-col>
            <v-row v-if="error">
              <span class="red--text text--darken-1">{{ errorMes }}</span>
            </v-row>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn dark color="green darken-1" @click="pay">
            PAY ({{ withdrawPrice }} TL)
          </v-btn>
          <v-btn dark color="red darken-1" @click="payDialog = false">
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
      withdrawPrice: "",
      errorMes: "",
      error: false,
      holderName: "",
      cardNumber: "",
      exDate: "",
      cvv: "",
      snackbar: false,
      mes: "",
      color: "",
      rentedMovies: [],
      favorites: [],
      actors: "",
      directors: "",
      genres: "",
      showedMovie: {},
      detailsDialog: false,
      payDialog: false,
      search: "",
      price: "",
      rate: "",
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
          filterable: false,
        },
        {
          text: "Due Date",
          sortable: false,
          align: "start",
          value: "due_date",
          filterable: false,
        },
        {
          text: "Renew Times",
          sortable: false,
          align: "start",
          value: "renew_times",
          filterable: false,
        },
        {
          text: "Rate",
          align: "start",
          value: "overall_rating",
          sortable: false,
          filter: (value) => {
            if (this.rate === "") {
              return true;
            } else {
              return value >= parseInt(this.rate);
            }
          },
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
          filter: (value) => {
            if (this.price === "") {
              return true;
            } else {
              return value <= parseInt(this.price);
            }
          },
        },
        {
          text: "Details",
          align: "center",
          value: "details",
          filterable: false,
          sortable: false,
        },
        {
          text: "Add Favorites",
          align: "center",
          value: "addFav",
          filterable: false,
          sortable: false,
        },
        {
          text: "",
          align: "center",
          value: "withdraw",
          filterable: false,
          sortable: false,
        },
        {
          text: "",
          align: "center",
          value: "renew",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    checkFav(item) {
      var check = false;
      this.favorites.forEach((elm) => {
        if (elm.movie_id === item.movie_id) {
          check = true;
        }
      });
      return check;
    },
    async addFavorites(value) {
      try {
        const res = await axiosInstance.post(URL.ADD_FAVORITE, {
          movieId: value.movie_id,
          userId: this.$store.state.uid,
        });
        console.log(res);
        await this.getFavorites();
      } catch (error) {
        console.log(error);
      }
    },
    async getFavorites() {
      try {
        const res = await axiosInstance.get(URL.GET_FAVORITE_LIST, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        this.favorites = res.data;
      } catch (error) {
        console.log("err");
      }
    },
    async withdraw(item) {
      const res = await axiosInstance.get(URL.GET_FEE, {
        params: {
          rentId: item.rent_id,
        },
      });
      this.withdrawPrice = res.data;
      if (this.$store.state.userType !== "Employee" && this.withdrawPrice !== 0) {
        this.payDialog = true;
      } else {
        try {
          const res = await axiosInstance.post(URL.WITHDRAW, {
            rentId: this.showedMovie.rent_id,
            userId: this.$store.state.uid,
          });
          this.mes = "The movie was withdrawn";
          this.snackbar = true;
          this.color = "green lighten-1";
          try {
            const res = await axiosInstance.get(URL.GET_ALL_RENTED_MOVIES, {
              params: {
                key: this.search,
                userId: this.$store.state.uid,
              },
            });
            this.rentedMovies = res.data;
          } catch (error) {
            console.log(error.response);
          }
          console.log(res);
        } catch (error) {
          console.log(error);
        }
      }
    },
    async pay() {
      var checkEx = this.checkExDate();
      var checkCVV = this.checkCVV();
      if (
        (this.holderName === "") |
        (this.cardNumber === "") |
        (this.exDate === "") |
        (this.cvv === "")
      ) {
        this.errorMes = "ALL FIELDS MUST BE FILLED";
        this.error = true;
      } else if (
        this.containsNumber(this.holderName) |
        isNaN(this.cardNumber) |
        !checkEx |
        !checkCVV |
        (this.cardNumber.length != 16)
      ) {
        this.errorMes = "FIELDS TYPES ARE NOT CORRECT";
        this.error = true;
      } else {
        this.error = false;
        this.snackbar = true;
        this.payDialog = false;
        this.holderName = "";
        this.cardNumber = "";
        this.exDate = "";
        this.exDate = "";
        this.cvv = "";
        this.color = "green lighten-1";

        try {
          const res = await axiosInstance.post(URL.WITHDRAW, {
            rentId: this.showedMovie.rent_id,
            userId: this.$store.state.uid,
          });
          this.mes = "The movie was withdrawn";
          this.snackbar = true;
          this.color = "green lighten-1";
          try {
            const res = await axiosInstance.get(URL.GET_ALL_RENTED_MOVIES, {
              params: {
                key: this.search,
                userId: this.$store.state.uid,
              },
            });
            this.rentedMovies = res.data;
          } catch (error) {
            console.log(error.response);
          }
          console.log(res);
        } catch (error) {
          console.log(error);
        }
      }
    },
    checkExDate() {
      if (this.exDate.length != 5) {
        return false;
      } else if (
        isNaN(this.exDate[0]) |
        isNaN(this.exDate[1]) |
        (this.exDate[2] != "/") |
        isNaN(this.exDate[3]) |
        isNaN(this.exDate[4])
      ) {
        return false;
      } else {
        return true;
      }
    },
    checkCVV() {
      if ((this.cvv.length != 3) | isNaN(this.cvv)) {
        return false;
      } else {
        return true;
      }
    },
    containsNumber(str) {
      return /\d/.test(str);
    },
    async renew() {
      try {
        const res = await axiosInstance.post(URL.RENEW, {
          rentId: this.showedMovie.rent_id,
          userId: this.$store.state.uid,
        });
        this.snackbar = true;
        this.mes = res.data.result;
        this.color = "green lighten-1";
        const response = await axiosInstance.get(URL.GET_ALL_RENTED_MOVIES, {
          params: {
            key: this.search,
            userId: this.$store.state.uid,
          },
        });
        this.rentedMovies = response.data;
      } catch (error) {
        this.snackbar = true;
        this.mes = error.response.data.result;
        this.color = "red lighten-1";
      }
    },
    showActors() {
      this.actors = "";
      this.showedMovie.actors.forEach((item) => {
        this.actors += item + ", ";
      });
    },
    showDirectors() {
      this.directors = "";
      this.showedMovie.directors.forEach((item) => {
        this.directors += item + ", ";
      });
    },
    showGenres() {
      this.genres = "";
      this.showedMovie.genres.forEach((item) => {
        this.genres += item + ", ";
      });
    },
    openDialog(item) {
      this.showedMovie = item;
      this.showActors();
      this.showDirectors();
      this.showGenres();
    },
  },
  async created() {
    try {
      const res = await axiosInstance.get(URL.GET_ALL_RENTED_MOVIES, {
        params: {
          key: this.search,
          userId: this.$store.state.uid,
        },
      });
      console.log(res);
      await this.getFavorites();

      this.rentedMovies = res.data;
    } catch (error) {
      console.log(error.response);
    }
  },
};
</script>
