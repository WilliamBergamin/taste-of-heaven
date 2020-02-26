import React from "react";
// eslint-disable-next-line no-unused-vars
import fetch from "isomorphic-unfetch";
import Cookies from "universal-cookie";
import { Grid } from "@material-ui/core";
import Link from "next/link";
import ToolBar from "../components/ToolBar";
import auth from "../Auth";
import YouMustLogIn from "../components/YouMustLogIn";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import EventIcon from "@material-ui/icons/Event";
import useListStyle from "../styles/eventSearchStyle";
import useIndexStyle from "../styles/IndexStyle";
import Paper from "@material-ui/core/Paper";
import TextField from "@material-ui/core/TextField";
import SearchIcon from "@material-ui/icons/Search";
import IconButton from "@material-ui/core/IconButton";

const baseURL = "http://3.133.81.46:80";

function ListItemLink(props) {
  return <ListItem button component="a" {...props} />;
}

class eventSearch extends React.Component {
  constructor(props) {
    super(props);
    auth.initialise(props);
    //   const cookies = new Cookies(req.cookies);
  }

  state = {
    allEvents: [],
    events: []
  };

  componentDidMount() {
    fetch(baseURL + "/api/v1/events", {
      method: "GET",
      headers: {
        Authorization: "Token " + auth.getIdToken()
      }
    })
      .then(res => res.json())
      .then(json => this.setState({ allEvents: json, events: json }));
  }

  render() {
    const { events } = this.state;
    return !auth.isAuthenticated() ? (
      <YouMustLogIn />
    ) : (
      <Grid
        container
        justify="center"
        style={{ width: "100%", marginTop: "5rem" }}
      >
        <Paper style={useListStyle.paper}>
          <ToolBar />
          <List
            style={{ width: "100%" }}
            subheader={
              <Grid
                container
                justify="center"
                direction="row"
                alignItems="center"
                style={{ width: "100%" }}
              >
                <TextField
                  style={{
                    marginLeft: "1%",
                    marginRight: "1%",
                    marginBottom: "1%"
                  }}
                  className={useIndexStyle.textField}
                  InputProps={{
                    className: useIndexStyle.input
                  }}
                  label="Search"
                  name="Search"
                  variant="outlined"
                  onChange={event => {
                    // setTempNumberOrder(parseInt(event.target.value.replace(/[^0-9]+/g, ''), 10));
                  }}
                  onKeyDown={e => {
                    if (e.keyCode === 13) {
                      // setNumberOrder(tempNumberOrder);
                    }
                  }}
                />
                <IconButton
                  style={{ marginBottom: "1%" }}
                  onClick={() => {
                    // setNumberOrder(tempNumberOrder);
                  }}
                >
                  <SearchIcon />
                </IconButton>
              </Grid>
            }
          >
            {events.map(event => (
              <Link
                key={event.event_key}
                href={{
                  pathname: "/event/[id]",
                  query: { event: JSON.stringify(event) }
                }}
                as={`/event/${event.event_key}`}
              >
                <ListItem button>
                  <ListItemIcon>
                    <EventIcon />
                  </ListItemIcon>
                  <ListItemText
                    primary={event.name}
                    secondary={event.location}
                  />
                </ListItem>
              </Link>
            ))}
          </List>
        </Paper>
      </Grid>
    );
  }
}

export default eventSearch;
