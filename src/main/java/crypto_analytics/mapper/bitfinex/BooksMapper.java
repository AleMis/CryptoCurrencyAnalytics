package crypto_analytics.mapper.bitfinex;

import crypto_analytics.domain.bitfinex.books.Books;
import crypto_analytics.domain.bitfinex.books.BooksDto;
import crypto_analytics.domain.bitfinex.candle.CandleDto;
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

    public BooksDto[] mapBooksListToBooksDtoArray(List<Books> booksList) {
        return booksList.stream().map(books -> new BooksDto(
                books.getPrice(),
                books.getCount(),
                books.getAmount()
        )).collect(Collectors.toList()).toArray(new BooksDto[booksList.size()]);
    }
}
