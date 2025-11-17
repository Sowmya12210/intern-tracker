terraform {
  backend "gcs" {
    bucket  = "tf-bucket2"
    prefix  = "intern-tracker"
  }
}
