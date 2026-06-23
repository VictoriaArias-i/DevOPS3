#!/bin/bash
set -e

echo "Desplegando en Kubernetes..."

kubectl apply -f k8s/cloudwatch-agent.yml
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/rbac.yml
kubectl apply -f k8s/storage-class.yml
kubectl apply -f k8s/secret.yml
kubectl apply -f k8s/configmap.yml
kubectl apply -f k8s/mysql-pvc.yml
kubectl apply -f k8s/mysql-deployment.yml

echo "Esperando que MySQL esté listo..."
kubectl rollout status deployment/mysql -n veterinaria --timeout=120s

kubectl apply -f k8s/app-deployment.yml
kubectl apply -f k8s/deployment.yml
kubectl apply -f k8s/service.yml
kubectl apply -f k8s/hpa.yml
kubectl apply -f k8s/network-policy.yml
kubectl apply -f k8s/ingress.yml

echo "Despliegue completado."
kubectl get all -n veterinaria
