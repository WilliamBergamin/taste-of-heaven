import theme from "../theme/Theme";

const useIndexStyle = {
  paper: {
    padding: "1% 1%",
    display: "flex",
    alignItems: "center",
    background: theme.palette.secondary.main
  },
  input: {
    marginLeft: 8,
    flex: 1,
    color: theme.palette.primary.main
  },
  fab: {
    position: "fixed",
    bottom: theme.spacing(2),
    left: theme.spacing(2)
  }
};

export default useIndexStyle;
