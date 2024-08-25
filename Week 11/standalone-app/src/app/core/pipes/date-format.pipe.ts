import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat',
  standalone: true
})
export class DateFormatPipe implements PipeTransform {
  transform(value: any, format: string = 'dd/MM/yyyy'): any {
    return new Date(value).toLocaleDateString('en-GB');
  }
}