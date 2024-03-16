package com.example.DeliverFee.service;

import com.example.DeliverFee.model.WeatherData;
import com.example.DeliverFee.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jakarta.annotation.PostConstruct;
import java.io.StringReader;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

// Defines this class as a Spring service and encapsulates the logic for fetching and parsing weather data.
@Service
public class WeatherDataService {

	private static final Logger log = LoggerFactory.getLogger(WeatherDataService.class);

	// Injects the WeatherDataRepository for CRUD operations on weather data.
  @Autowired
  private WeatherDataRepository weatherDataRepository;

	// Listing the specific weather stations outlined in the task.
  private static final List<String> STATION_NAMES = Arrays.asList("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

	// Intializing the first data fetch when program is started to get initial data before the schedualed fetches.
  @PostConstruct
  public void init() {
		fetchAndParseWeatherData();
  }

	// Scheduled task to fetch and parse weather data
  @Scheduled(cron = "0 15 * * * *") // At 15 minutes past the hour, every hour
  public void fetchAndParseWeatherData() {
		try {
			// Defining the URL for fetching weather data. Using RestTemplate to perform HTTP request. 
			String url = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
			RestTemplate restTemplate = new RestTemplate();
			String xmlString = restTemplate.getForObject(url, String.class);

			// Setting up XML parsing tools, 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Parsing the XML string into a Document object to traverse
			// and extract specific elements within the XML structure.
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

			// Extracting the timestamp from the observations element.
			Node observationsNode = doc.getElementsByTagName("observations").item(0);
			String timestampStr = ((Element) observationsNode).getAttribute("timestamp");
			long epochSecond = Long.parseLong(timestampStr);
			Instant observationsTimestamp = Instant.ofEpochSecond(epochSecond);

			// Starting traversal of all "station" nodes within the XML,
			// focusing on extracting data from specified weather stations.
			NodeList stationNodes = doc.getElementsByTagName("station");

			for (int i = 0; i < stationNodes.getLength(); i++) {
				Node node = stationNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String stationName = element.getElementsByTagName("name").item(0).getTextContent();

					
					// Filters the data to only include predefined stations.
					if (STATION_NAMES.contains(stationName)) {
						// Extracts weather data elements, converting textual data into their respective types for processing and storage.
						String wmocode = element.getElementsByTagName("wmocode").item(0).getTextContent();
						String airTemperatureStr = element.getElementsByTagName("airtemperature").item(0).getTextContent();
						String windSpeedStr = element.getElementsByTagName("windspeed").item(0).getTextContent();
						String phenomenon = element.getElementsByTagName("phenomenon").item(0).getTextContent();

						Double airTemperature = airTemperatureStr.isEmpty() ? null : Double.parseDouble(airTemperatureStr);
						Double windSpeed = windSpeedStr.isEmpty() ? null : Double.parseDouble(windSpeedStr);

						// Constructs a WeatherData object with the extracted data and saving the constructed WeatherData object to the repository.
						WeatherData weatherData = new WeatherData(stationName, wmocode, airTemperature, windSpeed, phenomenon, observationsTimestamp);
						weatherDataRepository.save(weatherData);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error while fetching and parsing weather data: ", e);
		}
  }
}