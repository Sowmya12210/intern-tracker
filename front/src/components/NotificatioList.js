import React from "react";

export default function NotificationList({ notifications }) {
  return (
    <ul>
      {notifications.map(n => <li key={n.id}>{n.message}</li>)}
    </ul>
  );
}
