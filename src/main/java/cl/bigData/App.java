package cl.bigData;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@ComponentScan(basePackages = "cl.bigData")
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "cl.bigData.repositories")
public class App
{
    public static void main( String[] args )
    {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
    }

    /* configuring the embbeded servlet to run in port 80*/
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.setPort(80);
        return tomcat;
    }


    /* It use a single node client to connect to elasticsearch, instead os the transport client
    * */
  /*  @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        Client client = NodeBuilder.nodeBuilder().local(true).node().client();
        return new ElasticsearchTemplate(client);
    }*/

}
