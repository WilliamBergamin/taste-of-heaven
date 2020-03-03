import React from "react";
import Link from "next/link";
import Button from "@material-ui/core/Button";

export default function YouMustLogIn() {
  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
      <Link href="/">
        <Button
          variant="outlined"
          size="small"
          color="primary"
          style={{ margin: "0.5rem" }}
        >
          You Must Log In
            </Button>
      </Link>
    </div>
  );
}
