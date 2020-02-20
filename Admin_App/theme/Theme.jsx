import { createMuiTheme } from '@material-ui/core/styles';
import grey from '@material-ui/core/colors/grey';

const theme = createMuiTheme({
  palette: {
    primary: {
      main: grey[900],
      contrastText: '#000',
    },
    secondary: {
      main: grey[200],
      contrastText: '#fff',
    },
  },
});

export default theme;
