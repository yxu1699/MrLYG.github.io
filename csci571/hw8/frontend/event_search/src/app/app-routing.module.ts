import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchformComponent } from './searchform/searchform.component';
import { AutocomComponent } from './autocom/autocom.component';
const routes: Routes = [
  { path: 'search', component: SearchformComponent },
  { path: 'test', component: AutocomComponent },
  // { path: '', redirectTo: '/search', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
