resource "google_sql_database_instance" "intern_tracker" {
  name             = "intern-tracker"
  database_version = "MYSQL_8_0"
  region           = var.region

  settings {
    tier = "db-f1-micro"
    backup_configuration { enabled = true }
  }
}

resource "google_sql_database" "service_db" {
  for_each = toset(var.services)
  name     = "${each.key}-service"
  instance = google_sql_database_instance.intern_tracker.name
}

resource "google_sql_user" "common_user" {
  name     = var.name
  instance = google_sql_database_instance.intern_tracker.name
  password = var.password
}

resource "google_artifact_registry_repository" "azure-repo" {
  location      = "us-west1"
  repository_id = "azure-repo"
  description   = "example docker repository"
  format        = "DOCKER"
}