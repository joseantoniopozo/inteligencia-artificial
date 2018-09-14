
ruta.dataset <- "C:\\Users\\A562245\\Desktop\\Master\\TIB\\trabajo\\dataset_final.csv"
print(paste("Fichero que contiene el dataset a cargar:",ruta.dataset))

#Cargamos el dataset
genes.cancer.dataset <- read.csv(ruta.dataset,header=TRUE,sep=",",row.names=1)
print(paste("Se ha cargado el dataset de expresiones de genes en tumores cancerígenos. Número de ejemplos:",nrow(genes.cancer.dataset),", número de atributos: ",length(genes.cancer.dataset)))

#El nombre de cada fila es la clase+identificador único
#Cargamos las clases (primera columna) en otra variable y la eliminamos del dataset
genes.cancer.dataset.class <- genes.cancer.dataset[1]
genes.cancer.dataset <- genes.cancer.dataset[-1]
#Distribución de frecuencias de las distintas clases
barplot(table(as.vector(t(genes.cancer.dataset.class))),col = c("red","green","blue","yellow","black"))

#Comprobamos que no haya valores vacíos, si es así, los informamos con la media de la columna
genes.cancer.dataset.omit <- na.omit(genes.cancer.dataset)
porcentaje.valores.vacios <- (1-(nrow(genes.cancer.dataset.omit)/nrow(genes.cancer.dataset)))
print(paste("Porcentaje de valores vacíos:",porcentaje.valores.vacios))

#Eliminamos los Genes (atributos) que tengan todos sus valores a cero
genes.valores.cero <- obtener_genes_a_cero(genes.cancer.dataset)
genes.cancer.dataset <- eliminar_atributos(genes.cancer.dataset,genes.valores.cero)
print(paste("Numero de atributos después de la eliminación:",length(genes.cancer.dataset)))

#Comprobamos el rango de varios atributo 
apply(genes.cancer.dataset[25:30], 2, range)

#En este caso, todos los atributos del dataset miden el mismo parámetro, el nivel de expresión de un gen
#por ello, no es necesario realizar un normalizado de los atributos

#Normalizamos los atributos
#genes.cancer.dataset.scaled <- normalize(genes.cancer.dataset, margin=2L)

#Comprobamos los rangos después de normalizar
#apply(genes.cancer.dataset.scaled[19:23], 2, range)

#Eliminamos valores outliers
library(MVN)
mvOutlier(genes.cancer.dataset)
#La función devuelve el siguiente error => Error in covMcd(data, alpha = alpha) : n <= p -- you can't be serious!

#Calculamos la matriz de distancia, utilizando la distancia euclidea
genes.cancer.dist<-dist(genes.cancer.dataset,method = "euclidean")

#Construimos el modelo de clustering jerárquico, con distancia completa
genes.cancer.modelo.hclust<-hclust(genes.cancer.dist,method = "complete")

#Pintamos el árbol jerárquico
plot(genes.cancer.modelo.hclust, hang = -1, cex = 0.6)

#Dividimos el árbol en 5 clústers, como el número de clases
rect.hclust(genes.cancer.modelo.hclust, k = 5, border = "red")

groups<-cutree(genes.cancer.modelo.hclust, k=5)
genes.cancer.dataset.groups<-cbind(groups,genes.cancer.dataset.class,genes.cancer.dataset)

#Obtenemos los datos de los 5 cluster
table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups)
barplot(table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups),col = c("red","green","blue","yellow","black"))
legend("topleft",         c("PRAD","LUAD","BRCA","KIRC","COAD"),                 fill = c("black","yellow","red","blue","green"))
print(paste("Porcentaje de elementos bien agrupados con la distancias Euclidea y Completa:",746/801))

#Construimos el modelo de clustering jerárquico, con distancia ward.D
genes.cancer.modelo.hclust<-hclust(genes.cancer.dist,method = "ward.D")

#Pintamos el árbol jerárquico
plot(genes.cancer.modelo.hclust, hang = -1, cex = 0.6)

#Dividimos el árbol en 5 clústers, como el número de clases
rect.hclust(genes.cancer.modelo.hclust, k = 5, border = "red")

groups<-cutree(genes.cancer.modelo.hclust, k=5)
genes.cancer.dataset.groups<-cbind(groups,genes.cancer.dataset.class,genes.cancer.dataset)

#Obtenemos los datos de los 5 cluster
table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups)
barplot(table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups),col = c("red","green","blue","yellow","black"))
legend("topleft",         c("PRAD","LUAD","BRCA","KIRC","COAD"),                 fill = c("black","yellow","red","blue","green"))
print(paste("Porcentaje de elementos bien agrupados con la distancias Euclidea y Ward.D:",797/801))


#Calculamos la matriz de distancia, utilizando la distancia manhattan
genes.cancer.dist<-dist(genes.cancer.dataset,method = "manhattan")

#Construimos el modelo de clustering jerárquico, con distancia completa
genes.cancer.modelo.hclust<-hclust(genes.cancer.dist,method = "complete")

#Pintamos el árbol jerárquico
plot(genes.cancer.modelo.hclust, hang = -1, cex = 0.6)

#Dividimos el árbol en 5 clústers, como el número de clases
rect.hclust(genes.cancer.modelo.hclust, k = 5, border = "red")

groups<-cutree(genes.cancer.modelo.hclust, k=5)
genes.cancer.dataset.groups<-cbind(groups,genes.cancer.dataset.class,genes.cancer.dataset)

#Obtenemos los datos de los 5 cluster
table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups)
barplot(table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups),col = c("red","green","blue","yellow","black"))
legend("topleft",         c("PRAD","LUAD","BRCA","KIRC","COAD"),                 fill = c("black","yellow","red","blue","green"))
print(paste("Porcentaje de elementos bien agrupados con la distancias Manhattan y Completa:",579/801))

#Construimos el modelo de clustering jerárquico, con distancia ward.D
genes.cancer.modelo.hclust<-hclust(genes.cancer.dist,method = "ward.D")

#Pintamos el árbol jerárquico
plot(genes.cancer.modelo.hclust, hang = -1, cex = 0.6)

#Dividimos el árbol en 5 clústers, como el número de clases
rect.hclust(genes.cancer.modelo.hclust, k = 5, border = "red")

groups<-cutree(genes.cancer.modelo.hclust, k=5)
genes.cancer.dataset.groups<-cbind(groups,genes.cancer.dataset.class,genes.cancer.dataset)

#Obtenemos los datos de los 5 cluster
table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups)
barplot(table(genes.cancer.dataset.groups$Class, genes.cancer.dataset.groups$groups),col = c("red","green","blue","yellow","black"))
legend("topleft",         c("PRAD","LUAD","BRCA","KIRC","COAD"),                 fill = c("black","yellow","red","blue","green"))
print(paste("Porcentaje de elementos bien agrupados con la distancias Manhattan y Ward.D:",798/801))


#Construimos el modelo kmeans
genes.cancer.modelo.kmeans <- kmeans(genes.cancer.dataset,5)
table(genes.cancer.modelo.kmeans$cluster, as.vector(t(genes.cancer.dataset.class)))
print(paste("Porcentaje de elementos bien agrupados con k-medias:",749/801))
barplot(table(as.vector(t(genes.cancer.dataset.class)),genes.cancer.modelo.kmeans$cluster),col = c("red","green","blue","yellow","black"))
legend("topleft",         c("PRAD","LUAD","BRCA","KIRC","COAD"),                 fill = c("black","yellow","red","blue","green"))

pos.var.max<-posicion_mayor(varianzas(genes.cancer.modelo.kmeans$centers))
print(paste("Gen con mayor varianza:",pos.var.max))

plot(genes.cancer.modelo.kmeans$centers[5,(pos.var.max-6):(pos.var.max+6)],type='overplotted',xlab='Gen', ylab='Expresión gen', xaxt = 'n', col="black")
lines(genes.cancer.modelo.kmeans$centers[2,(pos.var.max-6):(pos.var.max+6)], type='overplotted', col="blue")
lines(genes.cancer.modelo.kmeans$centers[1,(pos.var.max-6):(pos.var.max+6)], type='overplotted', col="green")
lines(genes.cancer.modelo.kmeans$centers[4,(pos.var.max-6):(pos.var.max+6)], type='overplotted', col="red")
lines(genes.cancer.modelo.kmeans$centers[3,(pos.var.max-6):(pos.var.max+6)], type='overplotted', col="yellow")
axis(side=1,at=c(1:13),labels=c((pos.var.max-6):(pos.var.max+6)))

pos.var.menor<-posicion_menor(varianzas(genes.cancer.modelo.kmeans$centers))
print(paste("Gen con menor varianza:",pos.var.menor))

plot(genes.cancer.modelo.kmeans$centers[3,(pos.var.menor-5):(pos.var.menor+5)],type='overplotted',xlab='Gen', ylab='Expresión gen', xaxt = 'n', col="black")
lines(genes.cancer.modelo.kmeans$centers[2,(pos.var.menor-5):(pos.var.menor+5)], type='overplotted', col="yellow")
lines(genes.cancer.modelo.kmeans$centers[1,(pos.var.menor-5):(pos.var.menor+5)], type='overplotted', col="green")
lines(genes.cancer.modelo.kmeans$centers[4,(pos.var.menor-5):(pos.var.menor+5)], type='overplotted', col="blue")
lines(genes.cancer.modelo.kmeans$centers[5,(pos.var.menor-5):(pos.var.menor+5)], type='overplotted', col="red")
axis(side=1,at=c(1:13),labels=c((pos.var.menor-6):(pos.var.menor+6)))

varianzas<-function(e) {
  lista <- c()
  atributos <- length(genes.cancer.dataset)
  for (gen in 1:atributos) {
    v<-var(c(e[1,gen],e[2,gen],e[3,gen],e[4,gen],e[5,gen]))
    lista<-c(lista,v)
  }
  return(lista)
}

posicion_mayor<-function(v){
  return(which(v == max(v)))  
}

posicion_menor<-function(v){
  return(which(v == min(v)))  
}

obtener_genes_a_cero<-function(e) {
  lista <- c()
  atributos <- length(genes.cancer.dataset)
  ejemplos <- nrow(genes.cancer.dataset)
  for (j in 1:atributos) {
    todo.cero=TRUE
    for (i in 1:ejemplos){
      if(e[i,j] != 0){
        todo.cero=FALSE
        break
      }
    }
    if(todo.cero){
      lista<-c(lista,j)
    }
  }
  return(lista)
}

eliminar_atributos<-function(dataset, atributos) {
  datos <- dataset
  lista.atributos <- sort(atributos, decreasing = TRUE)
  for (i in lista.atributos) {
    datos <- datos[-i]
  }
  return(datos)
}

