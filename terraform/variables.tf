variable "region" {
    default = "us-west1"
}
variable "name" {
    default = "root"
}

variable "password" {
    default = "iiitn123"
}

variable "services" {
  default = ["auth", "course", "intern", "notification"]
}

variable "project_id" {
    default = "fluted-factor-438905-d2"
}