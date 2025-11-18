import React, { useEffect, useState } from "react";
import { getNotifications } from "../api/notifications";
import NotificationList from "../components/NotificationList";

export default function Notifications({ token }) {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    getNotifications(token).then(setNotifications);
  }, [token]);

  return (
    <div>
      <h1>Notifications</h1>
      <NotificationList notifications={notifications} />
    </div>
  );
}
