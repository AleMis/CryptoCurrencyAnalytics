package crypto_analytics.mapper.bitfinex;


import crypto_analytics.domain.bitfinex.books.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BooksMapper {

   public List<Books> mapBooksDtoListToBooksList(HashMap<String, List<BooksDto>> booksDtoMap ) {
       List<Books> booksList = new ArrayList<>();
       for(Map.Entry<String, List<BooksDto>> map : booksDtoMap.entrySet()) {
            booksList = map.getValue().stream().map(booksDto ->  new Books(
                   null,
                   map.getKey(),
                   booksDto.getPrice(),
                   booksDto.getCount(),
                   booksDto.getAmount()
           )).collect(Collectors.toList());
       }
       return booksList;
    }

    public HashMap<String, List<BooksDto>> mapToBooksDtoList(HashMap<String, Object[][]> booksDataMap) {
       HashMap<String, List<BooksDto>> booksDtoToReturn = new HashMap<>();
        for(Map.Entry<String, Object[][]> booksData : booksDataMap.entrySet()) {
            List<BooksDto> booksDtoList = mapToBooksDtoListFromObject(booksData.getValue());
            booksDtoToReturn.put(booksData.getKey(), booksDtoList);
        }
        return booksDtoToReturn;
    }

    private List<BooksDto> mapToBooksDtoListFromObject(Object[][] object) {
        List<BooksDto> booksDtoList = new ArrayList<>();
        for (int i = 0; i < object.length; i++) {
            Double price = Double.valueOf(object[i][0].toString());
            Integer count = Integer.valueOf(object[i][1].toString());
            Double amount = Double.valueOf(object[i][2].toString());
            BooksDto booksDto = new BooksDto(price, count, amount);
            booksDtoList.add(booksDto);
        }
        return booksDtoList;
    }

    public MarketValues mapBooksListToBooksChartDto(List<Books> booksList) {
       String[][] asksMarketValues = getAsksMarketValues(booksList);
       String[][] bidsMarketValues = getBidsMarketValues(booksList);
        return  new MarketValues(asksMarketValues, bidsMarketValues);
    }

    private String[][] getAsksMarketValues(List<Books> booksList) {
        int marketValuesArraySize = getOfferQuantity(booksList);
        String[][] asksMarketValues = new String[marketValuesArraySize][2];
        int count = 0;
        for (Books books : booksList) {
            if (books.getAmount() < 0) {
                asksMarketValues[count][0] = books.getPrice().toString();
                Double amount = -books.getAmount();
                asksMarketValues[count][1] = amount.toString();
                count++;
            }
        }
        return asksMarketValues;
    }

    private String[][] getBidsMarketValues(List<Books> booksList) {
        int marketValuesArraySize = getOfferQuantity(booksList);
        String[][] bidsMarketValues = new String[marketValuesArraySize][2];
        int count = 0;
        for (Books books : booksList) {
            if (books.getAmount() > 0) {
                bidsMarketValues[count][0] = books.getPrice().toString();
                bidsMarketValues[count][1] = books.getAmount().toString();
                count++;
            }
        }
        return bidsMarketValues;
    }


    private int getOfferQuantity(List<Books> booksList) {
       int quantity = 0;
       for(Books books : booksList) {
           if(books.getAmount() > 0) {
               quantity++;
           }
       }
       return quantity;
    }
}
