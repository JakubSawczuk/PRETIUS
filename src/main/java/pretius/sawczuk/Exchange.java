package pretius.sawczuk;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class Exchange {

    public Exchange() {
        TimerTask timerTask = new JSONParser();
        Timer timer = new Timer(true);
        timer.schedule(timerTask, 0, 864 * 10000);
    }

    @RequestMapping(value = "/{count}/{fromCurrency}/{toCurrency}", method = RequestMethod.GET)
    public ResponseEntity<Recount> transferToLocalhost(
            @PathVariable("count") Double count,
            @PathVariable("fromCurrency") String fromCurrency,
            @PathVariable("toCurrency") String toCurrency) {

        Recount recount = new Recount();

        try {
            BigDecimal howManyToExchange = new BigDecimal(count * JSONParser.askMap.get(fromCurrency));
            BigDecimal result = howManyToExchange.
                    divide(new BigDecimal(JSONParser.bidMap.get(toCurrency)), 20, BigDecimal.ROUND_HALF_UP);

            recount.setResult(result);

            if (count < 1) {
                throw new ArithmeticException();
            }

        } catch (ResourceAccessException e) {
            System.out.println("Internet connection is failed");
        } catch (NullPointerException e) {
            System.out.println("This currency does not exist in table C");
        } catch (ArithmeticException e) {
            System.out.println("Count cannot be lesser than 1");
            recount.setResult(null);
        }
        return new ResponseEntity<>(recount, HttpStatus.OK);
    }
}
